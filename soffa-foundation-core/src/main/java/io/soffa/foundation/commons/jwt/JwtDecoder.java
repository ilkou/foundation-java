package io.soffa.foundation.commons.jwt;

import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.core.model.Authentication;
import io.soffa.foundation.core.model.TenantId;
import io.soffa.foundation.core.model.UserProfile;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public interface JwtDecoder {

    Optional<Authentication> decode(String jwt);

    default Authentication extractInfo(Jwt jwt) {
        TenantId tenant = jwt.lookupClaim("tenant").map(TenantId::new).orElse(null);

        UserProfile profile = new UserProfile();

        profile.setCity(jwt.lookupClaim("city", "location").orElse(null));
        profile.setCountry(jwt.lookupClaim("country", "countryId").orElse(null));
        profile.setGender(jwt.lookupClaim("gender", "sex", "sexe").orElse(null));
        profile.setEmail(jwt.lookupClaim("email", "mail").orElse(null));
        profile.setPhoneNumber(jwt.lookupClaim("mobile", "mobileNumber", "phoneNumber", "phone").orElse(null));
        profile.setGivenName(jwt.lookupClaim("givenname", "given_name", "firstname", "first_name", "prenom").orElse(null));
        profile.setFamilyName(jwt.lookupClaim("familyname", "family_name", "lastName", "last_name").orElse(null));
        profile.setNickname(jwt.lookupClaim("nickname", "nick_name", "pseudo", "alias").orElse(null));

        Set<String> permissions = new HashSet<>();
        Set<String> roles = new HashSet<>();

        jwt.lookupClaim("permissions", "grants").ifPresent(s -> {
            for (String item : s.split(",")) {
                if (TextUtil.isNotEmpty(item)) {
                    permissions.add(item.toLowerCase());
                }
            }
        });

        jwt.lookupClaim("roles").ifPresent(s -> {
            for (String item : s.split(",")) {
                if (TextUtil.isNotEmpty(item)) {
                    roles.add(item.toLowerCase());
                }
            }
        });

        return Authentication.builder().
            claims(jwt.getClaims()).
            username(jwt.getSubject()).
            tenantId(tenant).
            application(jwt.lookupClaim("applicationName", "application", "applicationId", "app").orElse(null)).
            profile(profile).
            roles(roles).
            permissions(permissions).
            build();
    }


}
