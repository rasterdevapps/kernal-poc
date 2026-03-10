import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ScreenService } from '../../../services/screen.service';
import { Screen, CreateScreenRequest } from '../../../models/screen.model';

@Component({
  selector: 'app-screen-list',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './screen-list.component.html',
  styleUrl: './screen-list.component.scss'
})
export class ScreenListComponent implements OnInit {
  private readonly screenService = inject(ScreenService);
  private readonly fb = inject(FormBuilder);

  readonly screens = signal<Screen[]>([]);
  readonly filteredScreens = signal<Screen[]>([]);
  readonly loading = signal(true);
  readonly showForm = signal(false);
  readonly editingId = signal<number | null>(null);
  readonly moduleFilter = signal('');

  readonly form: FormGroup = this.fb.group({
    screenId: ['', [Validators.required, Validators.maxLength(50)]],
    title: ['', [Validators.required, Validators.maxLength(100)]],
    description: ['', Validators.maxLength(255)],
    module: ['', [Validators.required, Validators.maxLength(50)]],
    tcodeId: [null],
    screenType: ['', [Validators.required, Validators.maxLength(50)]]
  });

  ngOnInit(): void {
    this.loadScreens();
  }

  loadScreens(): void {
    this.loading.set(true);
    this.screenService.findAll().subscribe({
      next: (screens) => {
        this.screens.set(screens);
        this.applyFilter();
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Failed to load screens', err);
        this.loading.set(false);
      }
    });
  }

  applyFilter(): void {
    const filter = this.moduleFilter().toLowerCase();
    if (filter) {
      this.filteredScreens.set(
        this.screens().filter(s => s.module.toLowerCase().includes(filter))
      );
    } else {
      this.filteredScreens.set(this.screens());
    }
  }

  onModuleFilterChange(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.moduleFilter.set(value);
    this.applyFilter();
  }

  openCreateForm(): void {
    this.editingId.set(null);
    this.form.reset();
    this.showForm.set(true);
  }

  openEditForm(screen: Screen): void {
    this.editingId.set(screen.id);
    this.form.patchValue({
      screenId: screen.screenId,
      title: screen.title,
      description: screen.description,
      module: screen.module,
      tcodeId: screen.tcodeId,
      screenType: screen.screenType
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

    const request: CreateScreenRequest = this.form.value;
    const id = this.editingId();

    if (id !== null) {
      this.screenService.update(id, request).subscribe({
        next: () => {
          this.cancelForm();
          this.loadScreens();
        }
      });
    } else {
      this.screenService.create(request).subscribe({
        next: () => {
          this.cancelForm();
          this.loadScreens();
        }
      });
    }
  }

  deleteScreen(id: number): void {
    if (!confirm('Are you sure you want to delete this screen?')) {
      return;
    }
    this.screenService.delete(id).subscribe({
      next: () => this.loadScreens()
    });
  }
}
