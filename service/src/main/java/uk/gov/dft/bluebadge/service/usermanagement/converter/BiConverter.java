package uk.gov.dft.bluebadge.service.usermanagement.converter;

interface BiConverter<ENTITY, MODEL> {
  ENTITY convertToEntity(MODEL model);

  MODEL convertToModel(ENTITY entity);
}
