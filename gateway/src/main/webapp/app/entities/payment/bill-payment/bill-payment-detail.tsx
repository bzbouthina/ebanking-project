import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bill-payment.reducer';

export const BillPaymentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const billPaymentEntity = useAppSelector(state => state.gateway.billPayment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="billPaymentDetailsHeading">
          <Translate contentKey="gatewayApp.paymentBillPayment.detail.title">BillPayment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{billPaymentEntity.id}</dd>
          <dt>
            <span id="billReference">
              <Translate contentKey="gatewayApp.paymentBillPayment.billReference">Bill Reference</Translate>
            </span>
          </dt>
          <dd>{billPaymentEntity.billReference}</dd>
          <dt>
            <span id="billIssuer">
              <Translate contentKey="gatewayApp.paymentBillPayment.billIssuer">Bill Issuer</Translate>
            </span>
          </dt>
          <dd>{billPaymentEntity.billIssuer}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="gatewayApp.paymentBillPayment.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{billPaymentEntity.amount}</dd>
          <dt>
            <span id="paymentDate">
              <Translate contentKey="gatewayApp.paymentBillPayment.paymentDate">Payment Date</Translate>
            </span>
          </dt>
          <dd>
            {billPaymentEntity.paymentDate ? (
              <TextFormat value={billPaymentEntity.paymentDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="gatewayApp.paymentBillPayment.description">Description</Translate>
            </span>
          </dt>
          <dd>{billPaymentEntity.description}</dd>
          <dt>
            <Translate contentKey="gatewayApp.paymentBillPayment.payment">Payment</Translate>
          </dt>
          <dd>{billPaymentEntity.payment ? billPaymentEntity.payment.paymentId : ''}</dd>
        </dl>
        <Button tag={Link} to="/bill-payment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bill-payment/${billPaymentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BillPaymentDetail;
