import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'game',
        loadChildren: () => import('./game/game.module').then(m => m.MicrolarpingGameModule)
      },
      {
        path: 'instance',
        loadChildren: () => import('./instance/instance.module').then(m => m.MicrolarpingInstanceModule)
      },
      {
        path: 'ruleset',
        loadChildren: () => import('./ruleset/ruleset.module').then(m => m.MicrolarpingRulesetModule)
      },
      {
        path: 'organizer',
        loadChildren: () => import('./organizer/organizer.module').then(m => m.MicrolarpingOrganizerModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class MicrolarpingEntityModule {}
