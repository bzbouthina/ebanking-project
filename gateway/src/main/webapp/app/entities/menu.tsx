import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate } from 'react-jhipster';
const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/customer">
        <Translate contentKey="global.menu.entities.customersCustomer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/document">
        <Translate contentKey="global.menu.entities.customersDocument" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/bank-account">
        <Translate contentKey="global.menu.entities.accountsBankAccount" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/localtransfer">
        <Translate contentKey="global.menu.entities.accountsLocaltransfer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/internatonal-transfer">
        <Translate contentKey="global.menu.entities.accountsInternatonalTransfer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment">
        <Translate contentKey="global.menu.entities.paymentPayment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/qr-payment">
        <Translate contentKey="global.menu.entities.paymentQrPayment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/bill-payment">
        <Translate contentKey="global.menu.entities.paymentBillPayment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/card-payment">
        <Translate contentKey="global.menu.entities.paymentCardPayment" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
