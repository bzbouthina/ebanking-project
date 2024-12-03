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

import { getEntities } from './card-payment.reducer';

export const CardPayment = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const cardPaymentList = useAppSelector(state => state.gateway.cardPayment.entities);
  const loading = useAppSelector(state => state.gateway.cardPayment.loading);
  const totalItems = useAppSelector(state => state.gateway.cardPayment.totalItems);

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
      <h2 id="card-payment-heading" data-cy="CardPaymentHeading">
        <Translate contentKey="gatewayApp.paymentCardPayment.home.title">Card Payments</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.paymentCardPayment.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/card-payment/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.paymentCardPayment.home.createLabel">Create new Card Payment</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cardPaymentList && cardPaymentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.paymentCardPayment.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('cardNumber')}>
                  <Translate contentKey="gatewayApp.paymentCardPayment.cardNumber">Card Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cardNumber')} />
                </th>
                <th className="hand" onClick={sort('cardExpiryDate')}>
                  <Translate contentKey="gatewayApp.paymentCardPayment.cardExpiryDate">Card Expiry Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cardExpiryDate')} />
                </th>
                <th className="hand" onClick={sort('cardHolderName')}>
                  <Translate contentKey="gatewayApp.paymentCardPayment.cardHolderName">Card Holder Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cardHolderName')} />
                </th>
                <th className="hand" onClick={sort('cvv')}>
                  <Translate contentKey="gatewayApp.paymentCardPayment.cvv">Cvv</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cvv')} />
                </th>
                <th className="hand" onClick={sort('amount')}>
                  <Translate contentKey="gatewayApp.paymentCardPayment.amount">Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amount')} />
                </th>
                <th className="hand" onClick={sort('paymentDate')}>
                  <Translate contentKey="gatewayApp.paymentCardPayment.paymentDate">Payment Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('paymentDate')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="gatewayApp.paymentCardPayment.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th>
                  <Translate contentKey="gatewayApp.paymentCardPayment.payment">Payment</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cardPaymentList.map((cardPayment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/card-payment/${cardPayment.id}`} color="link" size="sm">
                      {cardPayment.id}
                    </Button>
                  </td>
                  <td>{cardPayment.cardNumber}</td>
                  <td>
                    {cardPayment.cardExpiryDate ? (
                      <TextFormat type="date" value={cardPayment.cardExpiryDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{cardPayment.cardHolderName}</td>
                  <td>{cardPayment.cvv}</td>
                  <td>{cardPayment.amount}</td>
                  <td>
                    {cardPayment.paymentDate ? <TextFormat type="date" value={cardPayment.paymentDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{cardPayment.description}</td>
                  <td>
                    {cardPayment.payment ? <Link to={`/payment/${cardPayment.payment.id}`}>{cardPayment.payment.paymentId}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/card-payment/${cardPayment.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/card-payment/${cardPayment.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/card-payment/${cardPayment.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="gatewayApp.paymentCardPayment.home.notFound">No Card Payments found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={cardPaymentList && cardPaymentList.length > 0 ? '' : 'd-none'}>
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

export default CardPayment;
