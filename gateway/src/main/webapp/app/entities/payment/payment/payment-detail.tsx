import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment.reducer';

export const PaymentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentEntity = useAppSelector(state => state.gateway.payment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentDetailsHeading">
          <Translate contentKey="gatewayApp.paymentPayment.detail.title">Payment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.id}</dd>
          <dt>
            <span id="transactionId">
              <Translate contentKey="gatewayApp.paymentPayment.transactionId">Transaction Id</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.transactionId}</dd>
          <dt>
            <span id="login">
              <Translate contentKey="gatewayApp.paymentPayment.login">Login</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.login}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="gatewayApp.paymentPayment.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.amount}</dd>
          <dt>
            <span id="paymentType">
              <Translate contentKey="gatewayApp.paymentPayment.paymentType">Payment Type</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.paymentType}</dd>
          <dt>
            <span id="paymentDate">
              <Translate contentKey="gatewayApp.paymentPayment.paymentDate">Payment Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentEntity.paymentDate ? <TextFormat value={paymentEntity.paymentDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="gatewayApp.paymentPayment.description">Description</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.description}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.paymentPayment.status">Status</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.status}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="gatewayApp.paymentPayment.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="gatewayApp.paymentPayment.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentEntity.createdDate ? <TextFormat value={paymentEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="gatewayApp.paymentPayment.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="gatewayApp.paymentPayment.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentEntity.lastModifiedDate ? (
              <TextFormat value={paymentEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="paymentId">
              <Translate contentKey="gatewayApp.paymentPayment.paymentId">Payment Id</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.paymentId}</dd>
        </dl>
        <Button tag={Link} to="/payment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment/${paymentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentDetail;
