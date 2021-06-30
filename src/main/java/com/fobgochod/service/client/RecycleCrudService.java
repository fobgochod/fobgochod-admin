package com.fobgochod.service.client;

import com.fobgochod.entity.file.RecycleBin;
import com.fobgochod.service.client.base.EntityService;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.util.List;

public interface RecycleCrudService extends EntityService<RecycleBin> {

    List<RecycleBin> findLtDeleteDate(LocalDateTime deleteDate);

    List<RecycleBin> findAllSorted(Bson sort);
}
