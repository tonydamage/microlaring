import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRuleset } from 'app/shared/model/ruleset.model';

@Component({
  selector: 'jhi-ruleset-detail',
  templateUrl: './ruleset-detail.component.html'
})
export class RulesetDetailComponent implements OnInit {
  ruleset: IRuleset;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ ruleset }) => {
      this.ruleset = ruleset;
    });
  }

  previousState() {
    window.history.back();
  }
}
