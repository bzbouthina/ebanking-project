import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './customer.reducer';

export const Customer = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const customerList = useAppSelector(state => state.gateway.customer.entities);
  const loading = useAppSelector(state => state.gateway.customer.loading);
  const totalItems = useAppSelector(state => state.gateway.customer.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="customer-heading" data-cy="CustomerHeading">
        <Translate contentKey="gatewayApp.customersCustomer.home.title">Customers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.customersCustomer.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/customer/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.customersCustomer.home.createLabel">Create new Customer</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {customerList && customerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.customersCustomer.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('login')}>
                  <Translate contentKey="gatewayApp.customersCustomer.login">Login</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('login')} />
                </th>
                <th className="hand" onClick={sort('fullName')}>
                  <Translate contentKey="gatewayApp.customersCustomer.fullName">Full Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fullName')} />
                </th>
                <th className="hand" onClick={sort('motherName')}>
                  <Translate contentKey="gatewayApp.customersCustomer.motherName">Mother Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('motherName')} />
                </th>
                <th className="hand" onClick={sort('nationalId')}>
                  <Translate contentKey="gatewayApp.customersCustomer.nationalId">National Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nationalId')} />
                </th>
                <th className="hand" onClick={sort('dateOfBirth')}>
                  <Translate contentKey="gatewayApp.customersCustomer.dateOfBirth">Date Of Birth</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dateOfBirth')} />
                </th>
                <th className="hand" onClick={sort('gender')}>
                  <Translate contentKey="gatewayApp.customersCustomer.gender">Gender</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gender')} />
                </th>
                <th className="hand" onClick={sort('phoneNumber')}>
                  <Translate contentKey="gatewayApp.customersCustomer.phoneNumber">Phone Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phoneNumber')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="gatewayApp.customersCustomer.email">Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('addressLine1')}>
                  <Translate contentKey="gatewayApp.customersCustomer.addressLine1">Address Line 1</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addressLine1')} />
                </th>
                <th className="hand" onClick={sort('employmentStatus')}>
                  <Translate contentKey="gatewayApp.customersCustomer.employmentStatus">Employment Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('employmentStatus')} />
                </th>
                <th className="hand" onClick={sort('registrationDate')}>
                  <Translate contentKey="gatewayApp.customersCustomer.registrationDate">Registration Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('registrationDate')} />
                </th>
                <th className="hand" onClick={sort('lastUpdate')}>
                  <Translate contentKey="gatewayApp.customersCustomer.lastUpdate">Last Update</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdate')} />
                </th>
                <th className="hand" onClick={sort('nationality')}>
                  <Translate contentKey="gatewayApp.customersCustomer.nationality">Nationality</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nationality')} />
                </th>
                <th className="hand" onClick={sort('city')}>
                  <Translate contentKey="gatewayApp.customersCustomer.city">City</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('city')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {customerList.map((customer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/customer/${customer.id}`} color="link" size="sm">
                      {customer.id}
                    </Button>
                  </td>
                  <td>{customer.login}</td>
                  <td>{customer.fullName}</td>
                  <td>{customer.motherName}</td>
                  <td>{customer.nationalId}</td>
                  <td>
                    {customer.dateOfBirth ? <TextFormat type="date" value={customer.dateOfBirth} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    <Translate contentKey={`gatewayApp.Gender.${customer.gender}`} />
                  </td>
                  <td>{customer.phoneNumber}</td>
                  <td>{customer.email}</td>
                  <td>{customer.addressLine1}</td>
                  <td>
                    <Translate contentKey={`gatewayApp.EmploymentStatus.${customer.employmentStatus}`} />
                  </td>
                  <td>
                    {customer.registrationDate ? (
                      <TextFormat type="date" value={customer.registrationDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{customer.lastUpdate ? <TextFormat type="date" value={customer.lastUpdate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    <Translate contentKey={`gatewayApp.NationalityType.${customer.nationality}`} />
                  </td>
                  <td>
                    <Translate contentKey={`gatewayApp.CityType.${customer.city}`} />
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/customer/${customer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/customer/${customer.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/customer/${customer.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="gatewayApp.customersCustomer.home.notFound">No Customers found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={customerList && customerList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Customer;
