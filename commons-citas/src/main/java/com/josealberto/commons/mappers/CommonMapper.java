package com.josealberto.commons.mappers;

import org.springframework.stereotype.Component;

@Component
public interface CommonMapper<RQ, RS, E> {

    E requestAEntidad(RQ request);

    RS entidadAResponse(E entidad);
}
