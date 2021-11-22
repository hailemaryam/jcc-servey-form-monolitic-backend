jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMultipleChoiceAnsewer, MultipleChoiceAnsewer } from '../multiple-choice-ansewer.model';
import { MultipleChoiceAnsewerService } from '../service/multiple-choice-ansewer.service';

import { MultipleChoiceAnsewerRoutingResolveService } from './multiple-choice-ansewer-routing-resolve.service';

describe('MultipleChoiceAnsewer routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MultipleChoiceAnsewerRoutingResolveService;
  let service: MultipleChoiceAnsewerService;
  let resultMultipleChoiceAnsewer: IMultipleChoiceAnsewer | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(MultipleChoiceAnsewerRoutingResolveService);
    service = TestBed.inject(MultipleChoiceAnsewerService);
    resultMultipleChoiceAnsewer = undefined;
  });

  describe('resolve', () => {
    it('should return IMultipleChoiceAnsewer returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMultipleChoiceAnsewer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMultipleChoiceAnsewer).toEqual({ id: 123 });
    });

    it('should return new IMultipleChoiceAnsewer if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMultipleChoiceAnsewer = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMultipleChoiceAnsewer).toEqual(new MultipleChoiceAnsewer());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MultipleChoiceAnsewer })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMultipleChoiceAnsewer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMultipleChoiceAnsewer).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
