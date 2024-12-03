import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './document.reducer';

export const DocumentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const documentEntity = useAppSelector(state => state.gateway.document.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="documentDetailsHeading">
          <Translate contentKey="gatewayApp.customersDocument.detail.title">Document</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{documentEntity.id}</dd>
          <dt>
            <span id="documentNumber">
              <Translate contentKey="gatewayApp.customersDocument.documentNumber">Document Number</Translate>
            </span>
          </dt>
          <dd>{documentEntity.documentNumber}</dd>
          <dt>
            <span id="documentType">
              <Translate contentKey="gatewayApp.customersDocument.documentType">Document Type</Translate>
            </span>
          </dt>
          <dd>{documentEntity.documentType}</dd>
          <dt>
            <span id="issueDate">
              <Translate contentKey="gatewayApp.customersDocument.issueDate">Issue Date</Translate>
            </span>
          </dt>
          <dd>
            {documentEntity.issueDate ? <TextFormat value={documentEntity.issueDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="expiryDate">
              <Translate contentKey="gatewayApp.customersDocument.expiryDate">Expiry Date</Translate>
            </span>
          </dt>
          <dd>
            {documentEntity.expiryDate ? <TextFormat value={documentEntity.expiryDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="filePath">
              <Translate contentKey="gatewayApp.customersDocument.filePath">File Path</Translate>
            </span>
          </dt>
          <dd>{documentEntity.filePath}</dd>
          <dt>
            <span id="uploadedAt">
              <Translate contentKey="gatewayApp.customersDocument.uploadedAt">Uploaded At</Translate>
            </span>
          </dt>
          <dd>
            {documentEntity.uploadedAt ? <TextFormat value={documentEntity.uploadedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="gatewayApp.customersDocument.customer">Customer</Translate>
          </dt>
          <dd>{documentEntity.customer ? documentEntity.customer.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/document" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/document/${documentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocumentDetail;
