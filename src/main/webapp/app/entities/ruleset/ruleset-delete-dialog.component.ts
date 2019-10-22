import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRuleset } from 'app/shared/model/ruleset.model';
import { RulesetService } from './ruleset.service';

@Component({
  selector: 'jhi-ruleset-delete-dialog',
  templateUrl: './ruleset-delete-dialog.component.html'
})
export class RulesetDeleteDialogComponent {
  ruleset: IRuleset;

  constructor(protected rulesetService: RulesetService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.rulesetService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'rulesetListModification',
        content: 'Deleted an ruleset'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-ruleset-delete-popup',
  template: ''
})
export class RulesetDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ ruleset }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(RulesetDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.ruleset = ruleset;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/ruleset', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/ruleset', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
