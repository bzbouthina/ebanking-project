import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './localtransfer.reducer';

export const LocaltransferDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const localtransferEntity = useAppSelector(state => state.gateway.localtransfer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="localtransferDetailsHeading">
          <Translate contentKey="gatewayApp.accountsLocaltransfer.detail.title">Localtransfer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{localtransferEntity.id}</dd>
          <dt>
            <span id="senderAccountNumber">
              <Translate contentKey="gatewayApp.accountsLocaltransfer.senderAccountNumber">Sender Account Number</Translate>
            </span>
          </dt>
          <dd>{localtransferEntity.senderAccountNumber}</dd>
          <dt>
            <span id="recipientAccountNumber">
              <Translate contentKey="gatewayApp.accountsLocaltransfer.recipientAccountNumber">Recipient Account Number</Translate>
            </span>
          </dt>
          <dd>{localtransferEntity.recipientAccountNumber}</dd>
          <dt>
            <span id="recipientBankName">
              <Translate contentKey="gatewayApp.accountsLocaltransfer.recipientBankName">Recipient Bank Name</Translate>
            </span>
          </dt>
          <dd>{localtransferEntity.recipientBankName}</dd>
          <dt>
            <span id="recipientBankBranch">
              <Translate contentKey="gatewayApp.accountsLocaltransfer.recipientBankBranch">Recipient Bank Branch</Translate>
            </span>
          </dt>
          <dd>{localtransferEntity.recipientBankBranch}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="gatewayApp.accountsLocaltransfer.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{localtransferEntity.amount}</dd>
          <dt>
            <span id="transactionDate">
              <Translate contentKey="gatewayApp.accountsLocaltransfer.transactionDate">Transaction Date</Translate>
            </span>
          </dt>
          <dd>
            {localtransferEntity.transactionDate ? (
              <TextFormat value={localtransferEntity.transactionDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="gatewayApp.accountsLocaltransfer.description">Description</Translate>
            </span>
          </dt>
          <dd>{localtransferEntity.description}</dd>
          <dt>
            <Translate contentKey="gatewayApp.accountsLocaltransfer.bankAccount">Bank Account</Translate>
          </dt>
          <dd>{localtransferEntity.bankAccount ? localtransferEntity.bankAccount.accountNumber : ''}</dd>
        </dl>
        <Button tag={Link} to="/localtransfer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/localtransfer/${localtransferEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LocaltransferDetail;
