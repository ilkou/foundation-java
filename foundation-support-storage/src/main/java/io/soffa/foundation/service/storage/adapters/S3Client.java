package io.soffa.foundation.service.storage.adapters;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.soffa.foundation.commons.DateUtil;
import io.soffa.foundation.commons.Logger;
import io.soffa.foundation.errors.TechnicalException;
import io.soffa.foundation.service.storage.ObjectStorageClient;
import io.soffa.foundation.service.storage.model.ObjectStorageConfig;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@SuppressWarnings("WeakerAccess")
public class S3Client implements ObjectStorageClient {

    private static final Logger LOG = Logger.get(S3Client.class);
    private final AmazonS3 client;
    private final String defaultBucketName;

    public S3Client(ObjectStorageConfig cfg) {
        try {
            this.defaultBucketName = cfg.getBucket();
            AWSCredentials credentials = new BasicAWSCredentials(cfg.getAccessKey(), cfg.getSecretKey());
            ClientConfiguration config = new ClientConfiguration();
            config.setSignerOverride("AWSS3V4SignerType");
            String region = cfg.getRegion();
            if (StringUtils.isEmpty(region) || StringUtils.isBlank(region)) {
                region = Regions.US_EAST_1.name();
            }
            LOG.info("S3 Endpoint is: %s", cfg.getEndpoint());
            client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration(cfg.getEndpoint(), region))
                .withClientConfiguration(config).enablePathStyleAccess()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

        } catch (Exception e) {
            throw new TechnicalException("S3_CLIENT_INIT_ERR", e);
        }
    }


    @Override
    public void upload(InputStream source, String objectName, String contentType) {
        upload(source, defaultBucketName, objectName, contentType);
    }

    @SneakyThrows
    @Override
    public void upload(InputStream source, String bucket, String objectName, String contentType) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentEncoding(StandardCharsets.UTF_8.name());
            metadata.setContentType(contentType);
            client.putObject(bucket, objectName, source, metadata);
        } catch (Exception e) {
            throw new TechnicalException("S3_UPLOAD_ERROR", e);
        }
    }

    @Override
    public void upload(File source, String objectName) {
        upload(source, defaultBucketName, objectName);
    }

    @SneakyThrows
    @Override
    public void upload(File source, String bucket, String objectName) {
        try {
            client.putObject(bucket, objectName, source);
        } catch (Exception e) {
            throw new TechnicalException("S3_UPLOAD_ERROR", e);
        }
    }

    @SneakyThrows
    @Override
    public String downloadBase64(String bucket, String objectName) {
        return Base64.getEncoder().encodeToString(IOUtils.toByteArray(client.getObject(bucket, objectName).getObjectContent()));
    }

    @Override
    public String getDownloadUrl(String objectName, long expiresInMinutes) {
        return getDownloadUrl(defaultBucketName, objectName, expiresInMinutes);
    }

    @SneakyThrows
    @Override
    public String getDownloadUrl(String bucket, String objectName, long expiresInMinutes) {
        try {
            return client.generatePresignedUrl(bucket, objectName, DateUtil.plusHours(new Date(), 2)).toURI()
                .toString();
        } catch (Exception e) {
            throw new TechnicalException("S3_DOWNLOAD_URL_ERROR", e);
        }
    }

    @Override
    public String getUploadUrl(String objectName, long expiresInMinutes) {
        return getUploadUrl(defaultBucketName, objectName, expiresInMinutes);
    }

    @SneakyThrows
    @Override
    public String getUploadUrl(String bucket, String objectName, long expiresInMinutes) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, objectName)
            .withMethod(HttpMethod.PUT).withExpiration(DateUtil.plusHours(new Date(), 2));
        return client.generatePresignedUrl(generatePresignedUrlRequest).toURI().toString();
    }
}
