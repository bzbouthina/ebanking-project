import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './card-payment.reducer';

export const CardPaymentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cardPaymentEntity = useAppSelector(state => state.gateway.cardPayment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cardPaymentDetailsHeading">
          <Translate contentKey="gatewayApp.paymentCardPayment.detail.title">CardPayment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cardPaymentEntity.id}</dd>
          <dt>
            <span id="cardNumber">
              <Translate contentKey="gatewayApp.paymentCardPayment.cardNumber">Card Number</Translate>
            </span>
          </dt>
          <dd>{cardPaymentEntity.cardNumber}</dd>
          <dt>
            <span id="cardExpiryDate">
              <Translate contentKey="gatewayApp.paymentCardPayment.cardExpiryDate">Card Expiry Date</Translate>
            </span>
          </dt>
          <dd>
            {cardPaymentEntity.cardExpiryDate ? (
              <TextFormat value={cardPaymentEntity.cardExpiryDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="cardHolderName">
              <Translate contentKey="gatewayApp.paymentCardPayment.cardHolderName">Card Holder Name</Translate>
            </span>
          </dt>
          <dd>{cardPaymentEntity.cardHolderName}</dd>
          <dt>
            <span id="cvv">
              <Translate contentKey="gatewayApp.paymentCardPayment.cvv">Cvv</Translate>
            </span>
          </dt>
          <dd>{cardPaymentEntity.cvv}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="gatewayApp.paymentCardPayment.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{cardPaymentEntity.amount}</dd>
          <dt>
            <span id="paymentDate">
              <Translate contentKey="gatewayApp.paymentCardPayment.paymentDate">Payment Date</Translate>
            </span>
          </dt>
          <dd>
            {cardPaymentEntity.paymentDate ? (
              <TextFormat value={cardPaymentEntity.paymentDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="gatewayApp.paymentCardPayment.description">Description</Translate>
            </span>
          </dt>
          <dd>{cardPaymentEntity.description}</dd>
          <dt>
            <Translate contentKey="gatewayApp.paymentCardPayment.payment">Payment</Translate>
          </dt>
          <dd>{cardPaymentEntity.payment ? cardPaymentEntity.payment.paymentId : ''}</dd>
        </dl>
        <Button tag={Link} to="/card-payment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/card-payment/${cardPaymentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CardPaymentDetail;
