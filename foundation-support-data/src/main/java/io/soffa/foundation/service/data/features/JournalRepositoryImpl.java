package io.soffa.foundation.service.data.features;

import io.soffa.foundation.core.data.DB;
import io.soffa.foundation.core.features.journal.Journal;
import io.soffa.foundation.core.features.journal.JournalRepository;
import io.soffa.foundation.service.data.SimpleEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class JournalRepositoryImpl extends SimpleEntityRepository<Journal> implements JournalRepository {

    public JournalRepositoryImpl(DB db) {
        super(db, Journal.class);
    }

}
