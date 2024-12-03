import dayjs from 'dayjs';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { EmploymentStatus } from 'app/shared/model/enumerations/employment-status.model';
import { NationalityType } from 'app/shared/model/enumerations/nationality-type.model';
import { CityType } from 'app/shared/model/enumerations/city-type.model';

export interface ICustomer {
  id?: number;
  login?: string;
  fullName?: string;
  motherName?: string;
  nationalId?: string;
  dateOfBirth?: dayjs.Dayjs;
  gender?: keyof typeof Gender;
  phoneNumber?: string;
  email?: string;
  addressLine1?: string;
  employmentStatus?: keyof typeof EmploymentStatus;
  registrationDate?: dayjs.Dayjs;
  lastUpdate?: dayjs.Dayjs;
  nationality?: keyof typeof NationalityType;
  city?: keyof typeof CityType;
}

export const defaultValue: Readonly<ICustomer> = {};
