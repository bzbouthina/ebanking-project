import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bank-account.reducer';

export const BankAccountDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bankAccountEntity = useAppSelector(state => state.gateway.bankAccount.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bankAccountDetailsHeading">
          <Translate contentKey="gatewayApp.accountsBankAccount.detail.title">BankAccount</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bankAccountEntity.id}</dd>
          <dt>
            <span id="login">
              <Translate contentKey="gatewayApp.accountsBankAccount.login">Login</Translate>
            </span>
          </dt>
          <dd>{bankAccountEntity.login}</dd>
          <dt>
            <span id="accountNumber">
              <Translate contentKey="gatewayApp.accountsBankAccount.accountNumber">Account Number</Translate>
            </span>
          </dt>
          <dd>{bankAccountEntity.accountNumber}</dd>
          <dt>
            <span id="iban">
              <Translate contentKey="gatewayApp.accountsBankAccount.iban">Iban</Translate>
            </span>
          </dt>
          <dd>{bankAccountEntity.iban}</dd>
          <dt>
            <span id="balance">
              <Translate contentKey="gatewayApp.accountsBankAccount.balance">Balance</Translate>
            </span>
          </dt>
          <dd>{bankAccountEntity.balance}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="gatewayApp.accountsBankAccount.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{bankAccountEntity.currency}</dd>
          <dt>
            <span id="openingDate">
              <Translate contentKey="gatewayApp.accountsBankAccount.openingDate">Opening Date</Translate>
            </span>
          </dt>
          <dd>
            {bankAccountEntity.openingDate ? (
              <TextFormat value={bankAccountEntity.openingDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.accountsBankAccount.status">Status</Translate>
            </span>
          </dt>
          <dd>{bankAccountEntity.status}</dd>
          <dt>
            <span id="accountType">
              <Translate contentKey="gatewayApp.accountsBankAccount.accountType">Account Type</Translate>
            </span>
          </dt>
          <dd>{bankAccountEntity.accountType}</dd>
          <dt>
            <span id="branch">
              <Translate contentKey="gatewayApp.accountsBankAccount.branch">Branch</Translate>
            </span>
          </dt>
          <dd>{bankAccountEntity.branch}</dd>
        </dl>
        <Button tag={Link} to="/bank-account" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bank-account/${bankAccountEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BankAccountDetail;
