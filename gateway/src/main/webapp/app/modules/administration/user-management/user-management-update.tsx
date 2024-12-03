import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isEmail, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createUser, getRoles, getUser, reset, updateUser } from './user-management.reducer';
import { createEntity as createCustomer } from 'app/entities/customers/customer/customer.reducer';
import dayjs from 'dayjs';
import { AxiosResponse } from 'axios';
import { IUser } from 'app/shared/model/user.model';
import { ICustomer } from 'app/shared/model/customers/customer.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { EmploymentStatus } from 'app/shared/model/enumerations/employment-status.model';
import { NationalityType } from 'app/shared/model/enumerations/nationality-type.model';
import { CityType } from 'app/shared/model/enumerations/city-type.model';

interface IUserFormValues {
  id?: string;
  login: string;
  firstName?: string;
  lastName?: string;
  email: string;
  phoneNumber?: string;
  motherName?: string;
  addressLine1?: string;
  activated?: boolean;
  langKey?: string;
  authorities?: string[];
  gender?: keyof typeof Gender;
  employmentStatus?: keyof typeof EmploymentStatus;
  nationality?: keyof typeof NationalityType;
  city?: keyof typeof CityType;
}

export const UserManagementUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { login } = useParams<'login'>();
  const isNew = login === undefined;

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getUser(login));
    }
    dispatch(getRoles());
    return () => {
      dispatch(reset());
    };
  }, [login]);

  const handleClose = () => {
    navigate('/admin/user-management');
  };
  const saveUser = (values: IUserFormValues) => {
    const { phoneNumber, motherName, addressLine1, gender, employmentStatus, nationality, city, ...userValues } = values;

    if (!userValues.langKey) {
      userValues.langKey = 'en';
    }

    if (!userValues.authorities || userValues.authorities.length === 0) {
      userValues.authorities = ['ROLE_USER'];
    }

    if (isNew) {
      dispatch(createUser(userValues)).then(response => {
        const payload = response.payload as AxiosResponse<IUser>;
        if (payload.data && payload.data.id) {
          const customerData: ICustomer = {
            login: payload.data.login,
            email: payload.data.email,
            fullName: `${payload.data.firstName || ''} ${payload.data.lastName || ''}`,
            phoneNumber,
            motherName,
            addressLine1,
            registrationDate: dayjs(),
            dateOfBirth: dayjs().subtract(18, 'years'), // افتراض تاريخ ميلاد
            nationalId: Math.floor(Math.random() * 1000000000000).toString(), // رقم وطني عشوائي
            gender: gender || 'MALE',
            employmentStatus: employmentStatus || 'EMPLOYED',
            nationality: nationality || 'LIBYAN',
            city: city || 'TRIPOLI',
          };
          dispatch(createCustomer(customerData));
        }
      });
    } else {
      dispatch(updateUser(userValues));
    }
    handleClose();
  };
  const user = useAppSelector(state => state.userManagement.user);
  const loading = useAppSelector(state => state.userManagement.loading);
  const updating = useAppSelector(state => state.userManagement.updating);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>
            <Translate contentKey="userManagement.home.createOrEditLabel">Create or edit a User</Translate>
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm onSubmit={saveUser} defaultValues={user}>
              <ValidatedField
                type="text"
                name="login"
                label={translate('userManagement.login')}
                validate={{
                  required: { value: true, message: translate('register.messages.validate.login.required') },
                  pattern: {
                    value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+$/,
                    message: translate('register.messages.validate.login.pattern'),
                  },
                  maxLength: { value: 50, message: translate('register.messages.validate.login.maxlength') },
                }}
              />
              <ValidatedField
                type="text"
                name="firstName"
                label={translate('userManagement.firstName')}
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                type="text"
                name="lastName"
                label={translate('userManagement.lastName')}
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                name="email"
                label={translate('global.form.email.label')}
                placeholder={translate('global.form.email.placeholder')}
                type="email"
                validate={{
                  required: { value: true, message: translate('global.messages.validate.email.required') },
                  minLength: { value: 5, message: translate('global.messages.validate.email.minlength') },
                  maxLength: { value: 254, message: translate('global.messages.validate.email.maxlength') },
                  validate: v => isEmail(v) || translate('global.messages.validate.email.invalid'),
                }}
              />
              <ValidatedField
                type="text"
                name="phoneNumber"
                label={translate('gatewayApp.customersCustomer.phoneNumber')}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 10, message: translate('gatewayApp.customersCustomer.phoneNumberValidation') },
                  maxLength: { value: 15, message: translate('gatewayApp.customersCustomer.phoneNumberValidation') },
                  pattern: {
                    value: /^[+]?[0-9]{10,15}$/,
                    message: translate('entity.validation.pattern'),
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="motherName"
                label={translate('gatewayApp.customersCustomer.motherName')}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                type="text"
                name="addressLine1"
                label={translate('gatewayApp.customersCustomer.addressLine1')}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                type="select"
                name="langKey"
                label={translate('userManagement.langKey')}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              >
                <option value="" key="0" />
                <option value="en" key="1">
                  English
                </option>
                <option value="ar" key="2">
                  Arabic
                </option>
              </ValidatedField>
              <Button tag={Link} to="/admin/user-management" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <Translate contentKey="entity.action.back">Back</Translate>
              </Button>
              &nbsp;
              <Button color="primary" type="submit" disabled={updating}>
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

export default UserManagementUpdate;
