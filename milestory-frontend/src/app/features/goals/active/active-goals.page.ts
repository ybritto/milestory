import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-active-goals-page',
  imports: [RouterLink],
  templateUrl: './active-goals.page.html',
  styleUrl: './active-goals.page.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ActiveGoalsPage {}
