import dayjs from 'dayjs';
import { AccountStatus } from 'app/shared/model/enumerations/account-status.model';
import { AccountType } from 'app/shared/model/enumerations/account-type.model';

export interface IBankAccount {
  id?: number;
  login?: string;
  accountNumber?: string;
  iban?: string | null;
  balance?: number;
  currency?: string;
  openingDate?: dayjs.Dayjs;
  status?: keyof typeof AccountStatus;
  accountType?: keyof typeof AccountType;
  branch?: string;
}

export const defaultValue: Readonly<IBankAccount> = {};
