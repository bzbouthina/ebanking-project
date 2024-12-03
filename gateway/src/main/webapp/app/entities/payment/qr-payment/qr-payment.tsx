import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table, Modal, ModalHeader, ModalBody } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './qr-payment.reducer';
import { QRCodeSVG } from 'qrcode.react'; // Library for QR Code generation

export const QRPayment = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const qRPaymentList = useAppSelector(state => state.gateway.qRPayment.entities);
  const loading = useAppSelector(state => state.gateway.qRPayment.loading);
  const totalItems = useAppSelector(state => state.gateway.qRPayment.totalItems);

  const [selectedQRCode, setSelectedQRCode] = useState<string | null>(null);

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

  const handleGenerateQRCode = (id, amount, description) => {
    const qrData = `ID: ${id}, Amount: ${amount}, Description: ${description}`;
    setSelectedQRCode(qrData);
  };

  return (
    <div>
      <h2 id="qr-payment-heading" data-cy="QRPaymentHeading">
        <Translate contentKey="gatewayApp.paymentsQRPayment.home.title">QR Payments</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.paymentsQRPayment.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/qr-payment/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.paymentsQRPayment.home.createLabel">Create new QR Payment</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {qRPaymentList && qRPaymentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.paymentsQRPayment.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('amount')}>
                  <Translate contentKey="gatewayApp.paymentsQRPayment.amount">Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amount')} />
                </th>
                <th className="hand" onClick={sort('paymentDate')}>
                  <Translate contentKey="gatewayApp.paymentsQRPayment.paymentDate">Payment Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('paymentDate')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="gatewayApp.paymentsQRPayment.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th>
                  <Translate contentKey="gatewayApp.paymentsQRPayment.actions">Actions</Translate>
                </th>
              </tr>
            </thead>
            <tbody>
              {qRPaymentList.map((qRPayment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{qRPayment.id}</td>
                  <td>{qRPayment.amount}</td>
                  <td>
                    {qRPayment.paymentDate ? <TextFormat type="date" value={qRPayment.paymentDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{qRPayment.description}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        color="primary"
                        size="sm"
                        onClick={() => handleGenerateQRCode(qRPayment.id, qRPayment.amount, qRPayment.description)}
                      >
                        Generate QR Code
                      </Button>
                      <Button tag={Link} to={`/qr-payment/${qRPayment.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/qr-payment/${qRPayment.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        tag={Link}
                        to={`/qr-payment/${qRPayment.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="gatewayApp.paymentsQRPayment.home.notFound">No QR Payments found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={qRPaymentList && qRPaymentList.length > 0 ? '' : 'd-none'}>
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
      {selectedQRCode && (
        <Modal isOpen toggle={() => setSelectedQRCode(null)}>
          <ModalHeader toggle={() => setSelectedQRCode(null)}>Generated QR Code</ModalHeader>
          <ModalBody className="text-center">
            <QRCodeSVG value={selectedQRCode} size={250} />
          </ModalBody>
        </Modal>
      )}
    </div>
  );
};

export default QRPayment;
