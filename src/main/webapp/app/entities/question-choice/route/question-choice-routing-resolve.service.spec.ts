jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IQuestionChoice, QuestionChoice } from '../question-choice.model';
import { QuestionChoiceService } from '../service/question-choice.service';

import { QuestionChoiceRoutingResolveService } from './question-choice-routing-resolve.service';

describe('QuestionChoice routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: QuestionChoiceRoutingResolveService;
  let service: QuestionChoiceService;
  let resultQuestionChoice: IQuestionChoice | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(QuestionChoiceRoutingResolveService);
    service = TestBed.inject(QuestionChoiceService);
    resultQuestionChoice = undefined;
  });

  describe('resolve', () => {
    it('should return IQuestionChoice returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultQuestionChoice = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultQuestionChoice).toEqual({ id: 123 });
    });

    it('should return new IQuestionChoice if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultQuestionChoice = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultQuestionChoice).toEqual(new QuestionChoice());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as QuestionChoice })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultQuestionChoice = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultQuestionChoice).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
