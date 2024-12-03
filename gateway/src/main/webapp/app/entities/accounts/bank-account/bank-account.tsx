import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './bank-account.reducer';

export const BankAccount = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const bankAccountList = useAppSelector(state => state.gateway.bankAccount.entities);
  const loading = useAppSelector(state => state.gateway.bankAccount.loading);
  const totalItems = useAppSelector(state => state.gateway.bankAccount.totalItems);

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
      <h2 id="bank-account-heading" data-cy="BankAccountHeading">
        <Translate contentKey="gatewayApp.accountsBankAccount.home.title">Bank Accounts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.accountsBankAccount.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/bank-account/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.accountsBankAccount.home.createLabel">Create new Bank Account</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {bankAccountList && bankAccountList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.accountsBankAccount.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('login')}>
                  <Translate contentKey="gatewayApp.accountsBankAccount.login">Login</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('login')} />
                </th>
                <th className="hand" onClick={sort('accountNumber')}>
                  <Translate contentKey="gatewayApp.accountsBankAccount.accountNumber">Account Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('accountNumber')} />
                </th>
                <th className="hand" onClick={sort('iban')}>
                  <Translate contentKey="gatewayApp.accountsBankAccount.iban">Iban</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('iban')} />
                </th>
                <th className="hand" onClick={sort('balance')}>
                  <Translate contentKey="gatewayApp.accountsBankAccount.balance">Balance</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('balance')} />
                </th>
                <th className="hand" onClick={sort('currency')}>
                  <Translate contentKey="gatewayApp.accountsBankAccount.currency">Currency</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('currency')} />
                </th>
                <th className="hand" onClick={sort('openingDate')}>
                  <Translate contentKey="gatewayApp.accountsBankAccount.openingDate">Opening Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('openingDate')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="gatewayApp.accountsBankAccount.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('accountType')}>
                  <Translate contentKey="gatewayApp.accountsBankAccount.accountType">Account Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('accountType')} />
                </th>
                <th className="hand" onClick={sort('branch')}>
                  <Translate contentKey="gatewayApp.accountsBankAccount.branch">Branch</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('branch')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bankAccountList.map((bankAccount, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/bank-account/${bankAccount.id}`} color="link" size="sm">
                      {bankAccount.id}
                    </Button>
                  </td>
                  <td>{bankAccount.login}</td>
                  <td>{bankAccount.accountNumber}</td>
                  <td>{bankAccount.iban}</td>
                  <td>{bankAccount.balance}</td>
                  <td>{bankAccount.currency}</td>
                  <td>
                    {bankAccount.openingDate ? (
                      <TextFormat type="date" value={bankAccount.openingDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    <Translate contentKey={`gatewayApp.AccountStatus.${bankAccount.status}`} />
                  </td>
                  <td>
                    <Translate contentKey={`gatewayApp.AccountType.${bankAccount.accountType}`} />
                  </td>
                  <td>{bankAccount.branch}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/bank-account/${bankAccount.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/bank-account/${bankAccount.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/bank-account/${bankAccount.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="gatewayApp.accountsBankAccount.home.notFound">No Bank Accounts found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={bankAccountList && bankAccountList.length > 0 ? '' : 'd-none'}>
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

export default BankAccount;
