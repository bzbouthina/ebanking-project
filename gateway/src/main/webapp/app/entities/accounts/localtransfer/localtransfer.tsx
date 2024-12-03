import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './localtransfer.reducer';

export const Localtransfer = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const localtransferList = useAppSelector(state => state.gateway.localtransfer.entities);
  const loading = useAppSelector(state => state.gateway.localtransfer.loading);
  const totalItems = useAppSelector(state => state.gateway.localtransfer.totalItems);

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
      <h2 id="localtransfer-heading" data-cy="LocaltransferHeading">
        <Translate contentKey="gatewayApp.accountsLocaltransfer.home.title">Localtransfers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.accountsLocaltransfer.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/localtransfer/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.accountsLocaltransfer.home.createLabel">Create new Localtransfer</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {localtransferList && localtransferList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.accountsLocaltransfer.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('senderAccountNumber')}>
                  <Translate contentKey="gatewayApp.accountsLocaltransfer.senderAccountNumber">Sender Account Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('senderAccountNumber')} />
                </th>
                <th className="hand" onClick={sort('recipientAccountNumber')}>
                  <Translate contentKey="gatewayApp.accountsLocaltransfer.recipientAccountNumber">Recipient Account Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('recipientAccountNumber')} />
                </th>
                <th className="hand" onClick={sort('recipientBankName')}>
                  <Translate contentKey="gatewayApp.accountsLocaltransfer.recipientBankName">Recipient Bank Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('recipientBankName')} />
                </th>
                <th className="hand" onClick={sort('recipientBankBranch')}>
                  <Translate contentKey="gatewayApp.accountsLocaltransfer.recipientBankBranch">Recipient Bank Branch</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('recipientBankBranch')} />
                </th>
                <th className="hand" onClick={sort('amount')}>
                  <Translate contentKey="gatewayApp.accountsLocaltransfer.amount">Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amount')} />
                </th>
                <th className="hand" onClick={sort('transactionDate')}>
                  <Translate contentKey="gatewayApp.accountsLocaltransfer.transactionDate">Transaction Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('transactionDate')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="gatewayApp.accountsLocaltransfer.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th>
                  <Translate contentKey="gatewayApp.accountsLocaltransfer.bankAccount">Bank Account</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {localtransferList.map((localtransfer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/localtransfer/${localtransfer.id}`} color="link" size="sm">
                      {localtransfer.id}
                    </Button>
                  </td>
                  <td>{localtransfer.senderAccountNumber}</td>
                  <td>{localtransfer.recipientAccountNumber}</td>
                  <td>{localtransfer.recipientBankName}</td>
                  <td>{localtransfer.recipientBankBranch}</td>
                  <td>{localtransfer.amount}</td>
                  <td>
                    {localtransfer.transactionDate ? (
                      <TextFormat type="date" value={localtransfer.transactionDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{localtransfer.description}</td>
                  <td>
                    {localtransfer.bankAccount ? (
                      <Link to={`/bank-account/${localtransfer.bankAccount.id}`}>{localtransfer.bankAccount.accountNumber}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/localtransfer/${localtransfer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/localtransfer/${localtransfer.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/localtransfer/${localtransfer.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="gatewayApp.accountsLocaltransfer.home.notFound">No Localtransfers found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={localtransferList && localtransferList.length > 0 ? '' : 'd-none'}>
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

export default Localtransfer;
