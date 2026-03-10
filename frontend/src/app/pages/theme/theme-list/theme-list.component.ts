import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ThemeService } from '../../../services/theme.service';
import { Theme, CreateThemeRequest } from '../../../models/theme.model';

@Component({
  selector: 'app-theme-list',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './theme-list.component.html',
  styleUrl: './theme-list.component.scss'
})
export class ThemeListComponent implements OnInit {
  private readonly themeService = inject(ThemeService);
  private readonly fb = inject(FormBuilder);

  readonly themes = signal<Theme[]>([]);
  readonly loading = signal(true);
  readonly showForm = signal(false);
  readonly editingId = signal<number | null>(null);

  readonly form: FormGroup = this.fb.group({
    themeName: ['', [Validators.required, Validators.maxLength(100)]],
    description: ['', Validators.maxLength(255)],
    primaryColor: ['#354a5f', Validators.required],
    secondaryColor: ['#1a6fb5', Validators.required],
    isDefault: [false]
  });

  ngOnInit(): void {
    this.loadThemes();
  }

  loadThemes(): void {
    this.loading.set(true);
    this.themeService.findAll().subscribe({
      next: (themes) => {
        this.themes.set(themes);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Failed to load themes from API:', err.message, err);
        this.loading.set(false);
      }
    });
  }

  openCreateForm(): void {
    this.editingId.set(null);
    this.form.reset({ primaryColor: '#354a5f', secondaryColor: '#1a6fb5', isDefault: false });
    this.showForm.set(true);
  }

  openEditForm(theme: Theme): void {
    this.editingId.set(theme.id);
    this.form.patchValue({
      themeName: theme.themeName,
      description: theme.description,
      primaryColor: theme.primaryColor,
      secondaryColor: theme.secondaryColor,
      isDefault: theme.isDefault
    });
    this.showForm.set(true);
  }

  cancelForm(): void {
    this.showForm.set(false);
    this.form.reset();
    this.editingId.set(null);
  }

  onSubmit(): void {
    if (this.form.invalid) {
      return;
    }

    const request: CreateThemeRequest = this.form.value;
    const id = this.editingId();

    if (id !== null) {
      this.themeService.update(id, request).subscribe({
        next: () => {
          this.cancelForm();
          this.loadThemes();
        }
      });
    } else {
      this.themeService.create(request).subscribe({
        next: () => {
          this.cancelForm();
          this.loadThemes();
        }
      });
    }
  }

  deleteTheme(id: number): void {
    if (!confirm('Are you sure you want to delete this theme?')) {
      return;
    }
    this.themeService.delete(id).subscribe({
      next: () => this.loadThemes()
    });
  }
}
