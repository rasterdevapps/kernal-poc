import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserPreferenceService } from '../../services/user-preference.service';
import { ThemeService } from '../../services/theme.service';
import { Theme } from '../../models/theme.model';
import { UserPreference, CreateUserPreferenceRequest } from '../../models/user-preference.model';

@Component({
  selector: 'app-preferences',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './preferences.component.html',
  styleUrl: './preferences.component.scss'
})
export class PreferencesComponent implements OnInit {
  private readonly preferenceService = inject(UserPreferenceService);
  private readonly themeService = inject(ThemeService);
  private readonly fb = inject(FormBuilder);

  // TODO: Replace with dynamic user ID from authentication service
  private readonly currentUserId = 1;

  readonly themes = signal<Theme[]>([]);
  readonly preference = signal<UserPreference | null>(null);
  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly saved = signal(false);

  readonly form: FormGroup = this.fb.group({
    themeId: [null],
    locale: ['en-US', [Validators.required, Validators.maxLength(10)]],
    dateFormat: ['yyyy-MM-dd', [Validators.required, Validators.maxLength(20)]],
    timeFormat: ['HH:mm:ss', [Validators.required, Validators.maxLength(20)]],
    itemsPerPage: [20, [Validators.required, Validators.min(5), Validators.max(100)]]
  });

  readonly dateFormats = ['yyyy-MM-dd', 'dd/MM/yyyy', 'MM/dd/yyyy', 'dd.MM.yyyy'];
  readonly timeFormats = ['HH:mm:ss', 'HH:mm', 'hh:mm a', 'hh:mm:ss a'];
  readonly locales = ['en-US', 'en-GB', 'de-DE', 'fr-FR', 'es-ES', 'pt-BR', 'ja-JP', 'zh-CN'];

  ngOnInit(): void {
    this.loadData();
  }

  private loadData(): void {
    this.themeService.findAll().subscribe({
      next: (themes) => this.themes.set(themes),
      error: (err) => console.error('Failed to load themes from API:', err.message, err)
    });

    this.preferenceService.findByUserId(this.currentUserId).subscribe({
      next: (pref) => {
        this.preference.set(pref);
        this.form.patchValue({
          themeId: pref.themeId,
          locale: pref.locale,
          dateFormat: pref.dateFormat,
          timeFormat: pref.timeFormat,
          itemsPerPage: pref.itemsPerPage
        });
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Failed to load user preferences from API:', err.message, err);
        this.loading.set(false);
      }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      return;
    }

    this.saving.set(true);
    this.saved.set(false);

    const request: CreateUserPreferenceRequest = {
      userId: this.currentUserId,
      ...this.form.value
    };

    const pref = this.preference();
    if (pref) {
      this.preferenceService.update(pref.id, request).subscribe({
        next: () => {
          this.saving.set(false);
          this.saved.set(true);
        },
        error: (err) => {
          console.error('Failed to save user preferences to API:', err.message, err);
          this.saving.set(false);
        }
      });
    } else {
      this.preferenceService.create(request).subscribe({
        next: (created) => {
          this.preference.set(created);
          this.saving.set(false);
          this.saved.set(true);
        },
        error: (err) => {
          console.error('Failed to create user preferences via API:', err.message, err);
          this.saving.set(false);
        }
      });
    }
  }
}
