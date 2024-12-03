import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { Gender } from 'app/shared/model/enumerations/gender.model';
import { EmploymentStatus } from 'app/shared/model/enumerations/employment-status.model';
import { NationalityType } from 'app/shared/model/enumerations/nationality-type.model';
import { CityType } from 'app/shared/model/enumerations/city-type.model';
import { createEntity, getEntity, reset, updateEntity } from './customer.reducer';

export const CustomerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const customerEntity = useAppSelector(state => state.gateway.customer.entity);
  const loading = useAppSelector(state => state.gateway.customer.loading);
  const updating = useAppSelector(state => state.gateway.customer.updating);
  const updateSuccess = useAppSelector(state => state.gateway.customer.updateSuccess);
  const genderValues = Object.keys(Gender);
  const employmentStatusValues = Object.keys(EmploymentStatus);
  const nationalityTypeValues = Object.keys(NationalityType);
  const cityTypeValues = Object.keys(CityType);

  const handleClose = () => {
    navigate(`/customer${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    values.registrationDate = convertDateTimeToServer(values.registrationDate);
    values.lastUpdate = convertDateTimeToServer(values.lastUpdate);

    const entity = {
      ...customerEntity,
      ...values,
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
          registrationDate: displayDefaultDateTime(),
          lastUpdate: displayDefaultDateTime(),
        }
      : {
          gender: 'MALE',
          employmentStatus: 'EMPLOYED',
          nationality: 'LIBYAN',
          city: 'TRIPOLI',
          ...customerEntity,
          registrationDate: convertDateTimeFromServer(customerEntity.registrationDate),
          lastUpdate: convertDateTimeFromServer(customerEntity.lastUpdate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.customersCustomer.home.createOrEditLabel" data-cy="CustomerCreateUpdateHeading">
            <Translate contentKey="gatewayApp.customersCustomer.home.createOrEditLabel">Create or edit a Customer</Translate>
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
                  id="customer-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.customersCustomer.login')}
                id="customer-login"
                name="login"
                data-cy="login"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.customersCustomer.fullName')}
                id="customer-fullName"
                name="fullName"
                data-cy="fullName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.customersCustomer.motherName')}
                id="customer-motherName"
                name="motherName"
                data-cy="motherName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.customersCustomer.nationalId')}
                id="customer-nationalId"
                name="nationalId"
                data-cy="nationalId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 12, message: translate('entity.validation.minlength', { min: 12 }) },
                  maxLength: { value: 12, message: translate('entity.validation.maxlength', { max: 12 }) },
                  pattern: { value: /^[0-9]{12}$/, message: translate('entity.validation.pattern', { pattern: '^[0-9]{12}$' }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.customersCustomer.dateOfBirth')}
                id="customer-dateOfBirth"
                name="dateOfBirth"
                data-cy="dateOfBirth"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.customersCustomer.gender')}
                id="customer-gender"
                name="gender"
                data-cy="gender"
                type="select"
              >
                {genderValues.map(gender => (
                  <option value={gender} key={gender}>
                    {translate(`gatewayApp.Gender.${gender}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.customersCustomer.phoneNumber')}
                id="customer-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 10, message: translate('entity.validation.minlength', { min: 10 }) },
                  maxLength: { value: 15, message: translate('entity.validation.maxlength', { max: 15 }) },
                  pattern: {
                    value: /^[+]?[0-9]{10,15}$/,
                    message: translate('entity.validation.pattern', { pattern: '^[+]?[0-9]{10,15}$' }),
                  },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.customersCustomer.email')}
                id="customer-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: {
                    value: /^[^@]+@[^@]+\.[^@]+$/,
                    message: translate('entity.validation.pattern', { pattern: '^[^@]+@[^@]+\\.[^@]+$' }),
                  },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.customersCustomer.addressLine1')}
                id="customer-addressLine1"
                name="addressLine1"
                data-cy="addressLine1"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.customersCustomer.employmentStatus')}
                id="customer-employmentStatus"
                name="employmentStatus"
                data-cy="employmentStatus"
                type="select"
              >
                {employmentStatusValues.map(employmentStatus => (
                  <option value={employmentStatus} key={employmentStatus}>
                    {translate(`gatewayApp.EmploymentStatus.${employmentStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.customersCustomer.registrationDate')}
                id="customer-registrationDate"
                name="registrationDate"
                data-cy="registrationDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.customersCustomer.lastUpdate')}
                id="customer-lastUpdate"
                name="lastUpdate"
                data-cy="lastUpdate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.customersCustomer.nationality')}
                id="customer-nationality"
                name="nationality"
                data-cy="nationality"
                type="select"
              >
                {nationalityTypeValues.map(nationalityType => (
                  <option value={nationalityType} key={nationalityType}>
                    {translate(`gatewayApp.NationalityType.${nationalityType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.customersCustomer.city')}
                id="customer-city"
                name="city"
                data-cy="city"
                type="select"
              >
                {cityTypeValues.map(cityType => (
                  <option value={cityType} key={cityType}>
                    {translate(`gatewayApp.CityType.${cityType}`)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/customer" replace color="info">
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

export default CustomerUpdate;
