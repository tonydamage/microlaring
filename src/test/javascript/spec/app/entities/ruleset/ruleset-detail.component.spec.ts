import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MicrolarpingTestModule } from '../../../test.module';
import { RulesetDetailComponent } from 'app/entities/ruleset/ruleset-detail.component';
import { Ruleset } from 'app/shared/model/ruleset.model';

describe('Component Tests', () => {
  describe('Ruleset Management Detail Component', () => {
    let comp: RulesetDetailComponent;
    let fixture: ComponentFixture<RulesetDetailComponent>;
    const route = ({ data: of({ ruleset: new Ruleset(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrolarpingTestModule],
        declarations: [RulesetDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RulesetDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RulesetDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ruleset).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
