import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './qr-payment.reducer';

export const QRPaymentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const qRPaymentEntity = useAppSelector(state => state.gateway.qRPayment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="qRPaymentDetailsHeading">
          <Translate contentKey="gatewayApp.paymentQRPayment.detail.title">QRPayment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{qRPaymentEntity.id}</dd>
          <dt>
            <span id="qrCode">
              <Translate contentKey="gatewayApp.paymentQRPayment.qrCode">Qr Code</Translate>
            </span>
          </dt>
          <dd>{qRPaymentEntity.qrCode}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="gatewayApp.paymentQRPayment.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{qRPaymentEntity.amount}</dd>
          <dt>
            <span id="paymentDate">
              <Translate contentKey="gatewayApp.paymentQRPayment.paymentDate">Payment Date</Translate>
            </span>
          </dt>
          <dd>
            {qRPaymentEntity.paymentDate ? <TextFormat value={qRPaymentEntity.paymentDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="gatewayApp.paymentQRPayment.description">Description</Translate>
            </span>
          </dt>
          <dd>{qRPaymentEntity.description}</dd>
          <dt>
            <Translate contentKey="gatewayApp.paymentQRPayment.payment">Payment</Translate>
          </dt>
          <dd>{qRPaymentEntity.payment ? qRPaymentEntity.payment.paymentId : ''}</dd>
        </dl>
        <Button tag={Link} to="/qr-payment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/qr-payment/${qRPaymentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QRPaymentDetail;
