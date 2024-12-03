import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './customer.reducer';

export const CustomerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const customerEntity = useAppSelector(state => state.gateway.customer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="customerDetailsHeading">
          <Translate contentKey="gatewayApp.customersCustomer.detail.title">Customer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{customerEntity.id}</dd>
          <dt>
            <span id="login">
              <Translate contentKey="gatewayApp.customersCustomer.login">Login</Translate>
            </span>
          </dt>
          <dd>{customerEntity.login}</dd>
          <dt>
            <span id="fullName">
              <Translate contentKey="gatewayApp.customersCustomer.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{customerEntity.fullName}</dd>
          <dt>
            <span id="motherName">
              <Translate contentKey="gatewayApp.customersCustomer.motherName">Mother Name</Translate>
            </span>
          </dt>
          <dd>{customerEntity.motherName}</dd>
          <dt>
            <span id="nationalId">
              <Translate contentKey="gatewayApp.customersCustomer.nationalId">National Id</Translate>
            </span>
          </dt>
          <dd>{customerEntity.nationalId}</dd>
          <dt>
            <span id="dateOfBirth">
              <Translate contentKey="gatewayApp.customersCustomer.dateOfBirth">Date Of Birth</Translate>
            </span>
          </dt>
          <dd>
            {customerEntity.dateOfBirth ? (
              <TextFormat value={customerEntity.dateOfBirth} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="gender">
              <Translate contentKey="gatewayApp.customersCustomer.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{customerEntity.gender}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="gatewayApp.customersCustomer.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{customerEntity.phoneNumber}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="gatewayApp.customersCustomer.email">Email</Translate>
            </span>
          </dt>
          <dd>{customerEntity.email}</dd>
          <dt>
            <span id="addressLine1">
              <Translate contentKey="gatewayApp.customersCustomer.addressLine1">Address Line 1</Translate>
            </span>
          </dt>
          <dd>{customerEntity.addressLine1}</dd>
          <dt>
            <span id="employmentStatus">
              <Translate contentKey="gatewayApp.customersCustomer.employmentStatus">Employment Status</Translate>
            </span>
          </dt>
          <dd>{customerEntity.employmentStatus}</dd>
          <dt>
            <span id="registrationDate">
              <Translate contentKey="gatewayApp.customersCustomer.registrationDate">Registration Date</Translate>
            </span>
          </dt>
          <dd>
            {customerEntity.registrationDate ? (
              <TextFormat value={customerEntity.registrationDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastUpdate">
              <Translate contentKey="gatewayApp.customersCustomer.lastUpdate">Last Update</Translate>
            </span>
          </dt>
          <dd>
            {customerEntity.lastUpdate ? <TextFormat value={customerEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="nationality">
              <Translate contentKey="gatewayApp.customersCustomer.nationality">Nationality</Translate>
            </span>
          </dt>
          <dd>{customerEntity.nationality}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="gatewayApp.customersCustomer.city">City</Translate>
            </span>
          </dt>
          <dd>{customerEntity.city}</dd>
        </dl>
        <Button tag={Link} to="/customer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customer/${customerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CustomerDetail;
