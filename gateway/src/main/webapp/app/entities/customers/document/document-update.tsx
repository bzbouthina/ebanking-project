import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCustomers } from 'app/entities/customers/customer/customer.reducer';
import { DocumentType } from 'app/shared/model/enumerations/document-type.model';
import { createEntity, getEntity, reset, updateEntity } from './document.reducer';

export const DocumentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const customers = useAppSelector(state => state.gateway.customer.entities);
  const documentEntity = useAppSelector(state => state.gateway.document.entity);
  const loading = useAppSelector(state => state.gateway.document.loading);
  const updating = useAppSelector(state => state.gateway.document.updating);
  const updateSuccess = useAppSelector(state => state.gateway.document.updateSuccess);
  const documentTypeValues = Object.keys(DocumentType);

  const handleClose = () => {
    navigate(`/document${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCustomers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.uploadedAt = convertDateTimeToServer(values.uploadedAt);

    const entity = {
      ...documentEntity,
      ...values,
      customer: customers.find(it => it.id.toString() === values.customer?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          uploadedAt: displayDefaultDateTime(),
        }
      : {
          documentType: 'NATIONAL_ID',
          ...documentEntity,
          uploadedAt: convertDateTimeFromServer(documentEntity.uploadedAt),
          customer: documentEntity?.customer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.customersDocument.home.createOrEditLabel" data-cy="DocumentCreateUpdateHeading">
            <Translate contentKey="gatewayApp.customersDocument.home.createOrEditLabel">Create or edit a Document</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="document-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.customersDocument.documentNumber')}
                id="document-documentNumber"
                name="documentNumber"
                data-cy="documentNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.customersDocument.documentType')}
                id="document-documentType"
                name="documentType"
                data-cy="documentType"
                type="select"
              >
                {documentTypeValues.map(documentType => (
                  <option value={documentType} key={documentType}>
                    {translate(`gatewayApp.DocumentType.${documentType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.customersDocument.issueDate')}
                id="document-issueDate"
                name="issueDate"
                data-cy="issueDate"
                type="date"
              />
              <ValidatedField
                label={translate('gatewayApp.customersDocument.expiryDate')}
                id="document-expiryDate"
                name="expiryDate"
                data-cy="expiryDate"
                type="date"
              />
              <ValidatedField
                label={translate('gatewayApp.customersDocument.filePath')}
                id="document-filePath"
                name="filePath"
                data-cy="filePath"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.customersDocument.uploadedAt')}
                id="document-uploadedAt"
                name="uploadedAt"
                data-cy="uploadedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="document-customer"
                name="customer"
                data-cy="customer"
                label={translate('gatewayApp.customersDocument.customer')}
                type="select"
              >
                <option value="" key="0" />
                {customers
                  ? customers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/document" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DocumentUpdate;
