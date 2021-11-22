import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFormProgresss, FormProgresss } from '../form-progresss.model';

import { FormProgresssService } from './form-progresss.service';

describe('FormProgresss Service', () => {
  let service: FormProgresssService;
  let httpMock: HttpTestingController;
  let elemDefault: IFormProgresss;
  let expectedResult: IFormProgresss | IFormProgresss[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FormProgresssService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      submited: false,
      startedOn: currentDate,
      submitedOn: currentDate,
      sentedOn: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          startedOn: currentDate.format(DATE_TIME_FORMAT),
          submitedOn: currentDate.format(DATE_TIME_FORMAT),
          sentedOn: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a FormProgresss', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          startedOn: currentDate.format(DATE_TIME_FORMAT),
          submitedOn: currentDate.format(DATE_TIME_FORMAT),
          sentedOn: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startedOn: currentDate,
          submitedOn: currentDate,
          sentedOn: currentDate,
        },
        returnedFromService
      );

      service.create(new FormProgresss()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FormProgresss', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          submited: true,
          startedOn: currentDate.format(DATE_TIME_FORMAT),
          submitedOn: currentDate.format(DATE_TIME_FORMAT),
          sentedOn: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startedOn: currentDate,
          submitedOn: currentDate,
          sentedOn: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FormProgresss', () => {
      const patchObject = Object.assign(
        {
          submited: true,
          submitedOn: currentDate.format(DATE_TIME_FORMAT),
          sentedOn: currentDate.format(DATE_TIME_FORMAT),
        },
        new FormProgresss()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          startedOn: currentDate,
          submitedOn: currentDate,
          sentedOn: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FormProgresss', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          submited: true,
          startedOn: currentDate.format(DATE_TIME_FORMAT),
          submitedOn: currentDate.format(DATE_TIME_FORMAT),
          sentedOn: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startedOn: currentDate,
          submitedOn: currentDate,
          sentedOn: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a FormProgresss', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFormProgresssToCollectionIfMissing', () => {
      it('should add a FormProgresss to an empty array', () => {
        const formProgresss: IFormProgresss = { id: 123 };
        expectedResult = service.addFormProgresssToCollectionIfMissing([], formProgresss);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formProgresss);
      });

      it('should not add a FormProgresss to an array that contains it', () => {
        const formProgresss: IFormProgresss = { id: 123 };
        const formProgresssCollection: IFormProgresss[] = [
          {
            ...formProgresss,
          },
          { id: 456 },
        ];
        expectedResult = service.addFormProgresssToCollectionIfMissing(formProgresssCollection, formProgresss);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FormProgresss to an array that doesn't contain it", () => {
        const formProgresss: IFormProgresss = { id: 123 };
        const formProgresssCollection: IFormProgresss[] = [{ id: 456 }];
        expectedResult = service.addFormProgresssToCollectionIfMissing(formProgresssCollection, formProgresss);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formProgresss);
      });

      it('should add only unique FormProgresss to an array', () => {
        const formProgresssArray: IFormProgresss[] = [{ id: 123 }, { id: 456 }, { id: 93255 }];
        const formProgresssCollection: IFormProgresss[] = [{ id: 123 }];
        expectedResult = service.addFormProgresssToCollectionIfMissing(formProgresssCollection, ...formProgresssArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const formProgresss: IFormProgresss = { id: 123 };
        const formProgresss2: IFormProgresss = { id: 456 };
        expectedResult = service.addFormProgresssToCollectionIfMissing([], formProgresss, formProgresss2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formProgresss);
        expect(expectedResult).toContain(formProgresss2);
      });

      it('should accept null and undefined values', () => {
        const formProgresss: IFormProgresss = { id: 123 };
        expectedResult = service.addFormProgresssToCollectionIfMissing([], null, formProgresss, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formProgresss);
      });

      it('should return initial array if no FormProgresss is added', () => {
        const formProgresssCollection: IFormProgresss[] = [{ id: 123 }];
        expectedResult = service.addFormProgresssToCollectionIfMissing(formProgresssCollection, undefined, null);
        expect(expectedResult).toEqual(formProgresssCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
