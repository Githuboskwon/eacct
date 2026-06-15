package com.iljin.apiServer.ijeas.sm.jini;

import java.util.List;

public interface JiniQdslRepository {


    List<Jini> getJiniDocsByPreviousMonth(String vendor, String writer);

}
