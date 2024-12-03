import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './internatonal-transfer.reducer';

export const InternatonalTransferDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const internatonalTransferEntity = useAppSelector(state => state.gateway.internatonalTransfer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="internatonalTransferDetailsHeading">
          <Translate contentKey="gatewayApp.accountsInternatonalTransfer.detail.title">InternatonalTransfer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{internatonalTransferEntity.id}</dd>
          <dt>
            <span id="senderAccountNumber">
              <Translate contentKey="gatewayApp.accountsInternatonalTransfer.senderAccountNumber">Sender Account Number</Translate>
            </span>
          </dt>
          <dd>{internatonalTransferEntity.senderAccountNumber}</dd>
          <dt>
            <span id="recipientIban">
              <Translate contentKey="gatewayApp.accountsInternatonalTransfer.recipientIban">Recipient Iban</Translate>
            </span>
          </dt>
          <dd>{internatonalTransferEntity.recipientIban}</dd>
          <dt>
            <span id="swiftCode">
              <Translate contentKey="gatewayApp.accountsInternatonalTransfer.swiftCode">Swift Code</Translate>
            </span>
          </dt>
          <dd>{internatonalTransferEntity.swiftCode}</dd>
          <dt>
            <span id="recipientName">
              <Translate contentKey="gatewayApp.accountsInternatonalTransfer.recipientName">Recipient Name</Translate>
            </span>
          </dt>
          <dd>{internatonalTransferEntity.recipientName}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="gatewayApp.accountsInternatonalTransfer.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{internatonalTransferEntity.amount}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="gatewayApp.accountsInternatonalTransfer.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{internatonalTransferEntity.currency}</dd>
          <dt>
            <span id="transactionDate">
              <Translate contentKey="gatewayApp.accountsInternatonalTransfer.transactionDate">Transaction Date</Translate>
            </span>
          </dt>
          <dd>
            {internatonalTransferEntity.transactionDate ? (
              <TextFormat value={internatonalTransferEntity.transactionDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="gatewayApp.accountsInternatonalTransfer.description">Description</Translate>
            </span>
          </dt>
          <dd>{internatonalTransferEntity.description}</dd>
          <dt>
            <Translate contentKey="gatewayApp.accountsInternatonalTransfer.bankAccount">Bank Account</Translate>
          </dt>
          <dd>{internatonalTransferEntity.bankAccount ? internatonalTransferEntity.bankAccount.iban : ''}</dd>
        </dl>
        <Button tag={Link} to="/internatonal-transfer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/internatonal-transfer/${internatonalTransferEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InternatonalTransferDetail;
