package com.casper.sdk.service.impl.event;

import com.casper.sdk.model.event.DataType;
import com.casper.sdk.model.event.EventData;
import com.casper.sdk.model.event.EventTarget;
import lombok.ToString;

/**
 * An event that contains the event data as Pojo model returned from the event service when
 * {@link EventTarget#POJO} is requested.
 *
 * @param <T> the types of the expected EventData
 * @author ian@meywood.com
 */
@ToString(doNotUseGetters = true, callSuper = true)
final class PojoEvent<T extends EventData> extends AbstractEvent<T> {

    PojoEvent(final String source, final Long id, T data, final String version) {
        super(getDataType(data), source, id, data, version);
    }

    private static DataType getDataType(final Object data) {
        return DataType.of(data.getClass());
    }
}
