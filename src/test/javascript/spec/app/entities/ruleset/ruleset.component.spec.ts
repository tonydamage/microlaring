import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MicrolarpingTestModule } from '../../../test.module';
import { RulesetComponent } from 'app/entities/ruleset/ruleset.component';
import { RulesetService } from 'app/entities/ruleset/ruleset.service';
import { Ruleset } from 'app/shared/model/ruleset.model';

describe('Component Tests', () => {
  describe('Ruleset Management Component', () => {
    let comp: RulesetComponent;
    let fixture: ComponentFixture<RulesetComponent>;
    let service: RulesetService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrolarpingTestModule],
        declarations: [RulesetComponent],
        providers: []
      })
        .overrideTemplate(RulesetComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RulesetComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RulesetService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Ruleset(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.rulesets[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
