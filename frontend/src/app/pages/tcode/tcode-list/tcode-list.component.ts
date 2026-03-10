import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TCodeService } from '../../../services/tcode.service';
import { TCode, CreateTCodeRequest } from '../../../models/tcode.model';

@Component({
  selector: 'app-tcode-list',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './tcode-list.component.html',
  styleUrl: './tcode-list.component.scss'
})
export class TCodeListComponent implements OnInit {
  private readonly tcodeService = inject(TCodeService);
  private readonly fb = inject(FormBuilder);

  readonly tcodes = signal<TCode[]>([]);
  readonly filteredTCodes = signal<TCode[]>([]);
  readonly loading = signal(true);
  readonly showForm = signal(false);
  readonly editingId = signal<number | null>(null);
  readonly moduleFilter = signal('');

  readonly form: FormGroup = this.fb.group({
    code: ['', [Validators.required, Validators.maxLength(20)]],
    description: ['', [Validators.required, Validators.maxLength(255)]],
    module: ['', [Validators.required, Validators.maxLength(50)]],
    route: ['', [Validators.required, Validators.maxLength(255)]],
    icon: ['']
  });

  ngOnInit(): void {
    this.loadTCodes();
  }

  loadTCodes(): void {
    this.loading.set(true);
    this.tcodeService.findAll().subscribe({
      next: (tcodes) => {
        this.tcodes.set(tcodes);
        this.applyFilter();
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Failed to load T-Codes from API:', err.message, err);
        this.loading.set(false);
      }
    });
  }

  applyFilter(): void {
    const filter = this.moduleFilter().toLowerCase();
    if (filter) {
      this.filteredTCodes.set(
        this.tcodes().filter(t => t.module.toLowerCase().includes(filter))
      );
    } else {
      this.filteredTCodes.set(this.tcodes());
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

  openEditForm(tcode: TCode): void {
    this.editingId.set(tcode.id);
    this.form.patchValue({
      code: tcode.code,
      description: tcode.description,
      module: tcode.module,
      route: tcode.route,
      icon: tcode.icon
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

    const request: CreateTCodeRequest = this.form.value;
    const id = this.editingId();

    if (id !== null) {
      this.tcodeService.update(id, request).subscribe({
        next: () => {
          this.cancelForm();
          this.loadTCodes();
        }
      });
    } else {
      this.tcodeService.create(request).subscribe({
        next: () => {
          this.cancelForm();
          this.loadTCodes();
        }
      });
    }
  }

  deleteTCode(id: number): void {
    if (!confirm('Are you sure you want to delete this T-Code?')) {
      return;
    }
    this.tcodeService.delete(id).subscribe({
      next: () => this.loadTCodes()
    });
  }
}
