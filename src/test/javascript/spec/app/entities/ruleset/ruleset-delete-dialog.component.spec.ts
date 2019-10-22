import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MicrolarpingTestModule } from '../../../test.module';
import { RulesetDeleteDialogComponent } from 'app/entities/ruleset/ruleset-delete-dialog.component';
import { RulesetService } from 'app/entities/ruleset/ruleset.service';

describe('Component Tests', () => {
  describe('Ruleset Management Delete Component', () => {
    let comp: RulesetDeleteDialogComponent;
    let fixture: ComponentFixture<RulesetDeleteDialogComponent>;
    let service: RulesetService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrolarpingTestModule],
        declarations: [RulesetDeleteDialogComponent]
      })
        .overrideTemplate(RulesetDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RulesetDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RulesetService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
