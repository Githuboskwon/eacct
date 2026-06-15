package com.iljin.apiServer.ijeas.system.delegate;

import java.util.List;

public interface DelegateQdslRepository {

    List<DelegateDto> getDelegateList(DelegateDto delegateDto);

    List<DelegateDto> getPersonalList(DelegateDto delegateDto);
}
