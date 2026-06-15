package com.iljin.apiServer.ijeas.sm.jini;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface JiniService {

    @Modifying
    @Transactional
    String saveJiniDocs(List<JiniDto> jiniDtoList);

    @Transactional(readOnly = true)
    List<JiniDto> getJiniDocs(String documentId);

    @Modifying
    @Transactional
    String deleteJiniDocs(List<JiniDto> jiniDtoList);

    @Transactional(readOnly = true)
    List<JiniDto> getJiniDocsByPreviousMonth(String customer, String writer);

    @Modifying
    @Transactional
    String saveJiniDocsByBundle(BundleJiniDocs bundleJiniDocs);


}
