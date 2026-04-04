import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  signal,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { AbstractControl, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { ListGoalCategories200ResponseInner } from '../../../../api/model/listGoalCategories200ResponseInner';
import { GoalPlanningStore, GoalDraftInput } from '../shared/goal-planning.store';

@Component({
  selector: 'app-goal-create-page',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './goal-create.page.html',
  styleUrl: './goal-create.page.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GoalCreatePage {
  private readonly goalPlanningStore = inject(GoalPlanningStore);
  private readonly formBuilder = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly goalId = this.route.snapshot.paramMap.get('goalId');
  private hasSeededGoal = false;

  readonly goalCategories = this.goalPlanningStore.goalCategories;
  readonly viewState = this.goalPlanningStore.viewState;
  readonly currentGoal = this.goalPlanningStore.goal;
  readonly isEditing = computed(() => Boolean(this.goalId));
  readonly titleText = computed(() =>
    this.isEditing() ? 'Edit a yearly goal' : 'Create a yearly goal',
  );
  readonly form = this.formBuilder.group({
    title: this.formBuilder.nonNullable.control('', [Validators.required]),
    categoryMode: this.formBuilder.nonNullable.control<'starter' | 'custom'>('starter'),
    categoryId: this.formBuilder.nonNullable.control('', [Validators.required]),
    customCategoryName: this.formBuilder.nonNullable.control(''),
    targetValue: this.formBuilder.nonNullable.control(0, [Validators.required, Validators.min(1)]),
    unit: this.formBuilder.nonNullable.control('', [Validators.required]),
    motivation: this.formBuilder.nonNullable.control('', [Validators.required]),
    notes: this.formBuilder.nonNullable.control('', [Validators.required]),
  });
  readonly selectedMode = signal<'starter' | 'custom'>('starter');
  constructor() {
    this.syncCategoryValidators(this.form.controls.categoryMode.value);
    this.goalPlanningStore.loadGoalCategories();

    if (this.goalId) {
      this.goalPlanningStore.loadGoal(this.goalId);
    }

    effect(() => {
      const goal = this.currentGoal();

      if (!this.goalId || !goal || this.hasSeededGoal) {
        return;
      }

      this.hasSeededGoal = true;
      this.form.patchValue({
        title: goal.title,
        categoryMode: 'starter',
        categoryId: goal.categoryId,
        customCategoryName: '',
        targetValue: goal.targetValue,
        unit: goal.unit,
        motivation: goal.motivation,
        notes: goal.notes,
      });
      this.selectedMode.set('starter');
      this.syncCategoryValidators('starter');
    });

    effect(() => {
      if (this.form.controls.categoryMode.value !== this.selectedMode()) {
        this.selectedMode.set(this.form.controls.categoryMode.value);
        this.syncCategoryValidators(this.form.controls.categoryMode.value);
      }
    });
  }

  selectCategory(category: ListGoalCategories200ResponseInner): void {
    this.form.patchValue({
      categoryMode: 'starter',
      categoryId: category.categoryId,
      customCategoryName: '',
    });
  }

  selectCustomCategory(): void {
    this.form.patchValue({
      categoryMode: 'custom',
      categoryId: '',
      customCategoryName: '',
    });
  }

  continue(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const draft = this.form.getRawValue() as GoalDraftInput;
    const nextRoute = this.goalId ? ['/goals', this.goalId, 'edit', 'plan'] : ['/goals/new/plan'];

    this.goalPlanningStore.previewDraft(draft).subscribe({
      next: () => {
        void this.router.navigate(nextRoute);
      },
    });
  }

  isSelectedCategory(categoryId: string): boolean {
    return this.form.controls.categoryId.value === categoryId;
  }

  protected hasError(field: GoalCreateField): boolean {
    return Boolean(this.errorMessage(field));
  }

  protected errorMessage(field: GoalCreateField): string {
    switch (field) {
      case 'title':
        return this.controlError(this.form.controls.title, {
          required: 'Goal title is required.',
        });
      case 'category':
        return this.form.controls.categoryMode.value === 'custom'
          ? this.controlError(this.form.controls.customCategoryName, {
              required: 'Custom category name is required.',
            })
          : this.controlError(this.form.controls.categoryId, {
              required: 'Choose a starter category or create a custom one.',
            });
      case 'targetValue':
        return this.controlError(this.form.controls.targetValue, {
          required: 'Target value is required.',
          min: 'Target value must be greater than 0.',
        });
      case 'unit':
        return this.controlError(this.form.controls.unit, {
          required: 'Unit is required.',
        });
      case 'motivation':
        return this.controlError(this.form.controls.motivation, {
          required: 'Motivation is required.',
        });
      case 'notes':
        return this.controlError(this.form.controls.notes, {
          required: 'Notes are required.',
        });
    }
  }

  private syncCategoryValidators(mode: 'starter' | 'custom'): void {
    if (mode === 'custom') {
      this.form.controls.categoryId.clearValidators();
      this.form.controls.customCategoryName.setValidators([Validators.required]);
    } else {
      this.form.controls.categoryId.setValidators([Validators.required]);
      this.form.controls.customCategoryName.clearValidators();
    }

    this.form.controls.categoryId.updateValueAndValidity({ emitEvent: false });
    this.form.controls.customCategoryName.updateValueAndValidity({ emitEvent: false });
  }

  private controlError(
    control: AbstractControl<string | number>,
    messages: Record<string, string>,
  ): string {
    if (!control.touched || !control.errors) {
      return '';
    }

    for (const [errorKey, message] of Object.entries(messages)) {
      if (control.hasError(errorKey)) {
        return message;
      }
    }

    return '';
  }
}

type GoalCreateField = 'title' | 'category' | 'targetValue' | 'unit' | 'motivation' | 'notes';
