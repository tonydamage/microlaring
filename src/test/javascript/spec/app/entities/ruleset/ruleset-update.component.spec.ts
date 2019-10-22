import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MicrolarpingTestModule } from '../../../test.module';
import { RulesetUpdateComponent } from 'app/entities/ruleset/ruleset-update.component';
import { RulesetService } from 'app/entities/ruleset/ruleset.service';
import { Ruleset } from 'app/shared/model/ruleset.model';

describe('Component Tests', () => {
  describe('Ruleset Management Update Component', () => {
    let comp: RulesetUpdateComponent;
    let fixture: ComponentFixture<RulesetUpdateComponent>;
    let service: RulesetService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrolarpingTestModule],
        declarations: [RulesetUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(RulesetUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RulesetUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RulesetService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Ruleset(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Ruleset();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
