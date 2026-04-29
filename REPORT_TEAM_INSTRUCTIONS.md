# Compost Monitoring System Report Instructions

## Purpose
This file explains how to write the mini-project report using the root PDF template `TISD_Mini_Project_report_format.pdf`. It is intended for the two teammates who will produce the report.

## Report Structure
Use the PDF template exactly as the format: cover page, approval sheet, declaration, abstract, acknowledgments, table of contents, chapters, references, and appendices.

### Required Sections
1. Cover page
2. Approval sheet
3. Declaration
4. Abstract
5. Acknowledgments
6. Table of Contents
7. List of Tables / List of Figures
8. Chapter 1: Introduction
9. Chapter 2: SDG Mapping
10. Chapter 3: System Design
11. Chapter 4: Implementation
12. Chapter 5: Results
13. Chapter 6: Conclusion and Future Scope
14. References
15. Appendices I-V

## Team Roles
### Teammate 1: Backend-focused author
**Primary responsibilities:** Chapter 3, Chapter 4, Chapter 5 technical results, backend architecture, and data processing details.

#### Deliverables
- Chapter 3: System Design
  - Block diagram of the compost monitoring system
  - Module descriptions for backend, MQTT, database, REST API, and batch processing logic
  - Algorithms used for batch progress, alerts, and request handling
  - Hardware/software requirements table for the backend stack
  - Database implementation notes (MongoDB collections, documents, queries)

- Chapter 4: Implementation
  - Detailed account of backend implementation steps
  - Spring Boot services, controllers, models, and MQTT subscriber behavior
  - How sensor readings are stored and how compost batch state is updated
  - Any backend-specific issues or configuration details

- Chapter 5: Results
  - Key outcomes achieved by the system (real-time sensor capture, batching logic, request workflow)
  - Comparison of existing compost monitoring/management systems vs our solution
  - Challenges faced during backend implementation and strategies used to overcome them
  - Observations and impact analysis on system reliability and data flow

- Appendices
  - Appendix I: Database schema or sample payloads
  - Appendix IV: Concept map for backend architecture and data flow

### Teammate 2: Frontend-focused author
**Primary responsibilities:** chapters that describe UI, user interaction, mobile UX, and overall system experience.

#### Deliverables
- Chapter 1: Introduction
  - Background of the compost monitoring problem
  - Review of literature / existing solutions in a table format
  - Problem statement for a smart compost monitoring machine
  - Objectives and scope of the project

- Chapter 2: SDG Mapping
  - Primary SDG(s) relevant to composting, waste reduction, clean energy, and sustainable cities
  - Additional SDGs if applicable
  - Mapping explanation of how the system supports these goals

- Chapter 4: Implementation
  - Frontend implementation details: React, Vite, dashboard, history view, request form, notification system
  - Mobile-first and Android-directed UX improvements
  - How auth infrastructure is implemented but disabled for now
  - How alert and sensor history features were designed in the UI

- Chapter 5: Results
  - Key outcomes from the frontend perspective: usable dashboard, mobile responsiveness, notification experience
  - UI/UX comparisons with generic monitoring dashboards
  - Frontend-specific challenges and solutions

- Appendices
  - Appendix II: Screenshots of the app and UI flows
  - Appendix III: Participation certificates or project presentations if available

## Shared Responsibilities
Both teammates should collaborate on the following:
- Abstract: one paragraph summary of the project, problem statement, proposed solution, and mapped SDGs
- References: collect citations for any external sources, libraries, frameworks, and papers used
- Appendix V: Plagiarism report placeholder
- Review and edit the overall report for consistency and cohesion

## Content Guidance by Section
### Abstract
Include:
- Project summary
- Key problem statement and solution approach
- SDG mapping sentence(s)
- Important keywords separated by commas

### Introduction
Include:
- Problem background: composting difficulty, need for monitoring, rural/urban farming impact
- Review of literature: similar compost monitoring, IoT agriculture, sensor-based systems
- Problem statement: need for real-time compost health monitoring and batch management
- Objectives: sensor data capture, alert system, batch progress tracking, farmer request support, mobile-friendly UI
- Scope: single microcontroller machine, backend analytics, frontend dashboard, batch lifecycle management

### SDG Mapping
Possible SDGs:
- Primary: SDG 12 (Responsible Consumption and Production)
- Additional: SDG 11 (Sustainable Cities and Communities), SDG 13 (Climate Action), SDG 15 (Life on Land)
- Explain how compost monitoring reduces waste, supports sustainable agriculture, and enables cleaner resource reuse.

### System Design
Include:
- Overall block diagram (sensor -> MCU -> MQTT -> backend -> database -> frontend)
- Modules: Sensor Acquisition, MQTT Broker, Backend Service, Database, Frontend Dashboard, Notification System
- Algorithms: progress update, alert thresholds, compost yield calculation, batch request handling
- Hardware and software requirements in a table

### Implementation
Describe actual code-level work:
- Backend: Spring Boot controllers, services, MQTT subscription, MongoDB models
- Frontend: React components, polling strategy, charting plan, auth framework disabled
- Mention any integration notes and API contract

### Results
Describe:
- Functional results achieved
- System behavior under test data or mock sensor data
- Comparison to existing systems if possible
- Challenges and impact analysis

### Conclusion and Future Scope
Cover:
- Project summary
- Limitations and areas to improve
- Future enhancements such as multiple bins, AI-based prediction, full auth, PWA push notifications, real device integration

## Practical Notes
- Use Times New Roman font for main text
- Keep chapter titles bold and justified
- Use roman page numbering before Chapter 1 and decimal numbering from Chapter 1 onward
- Insert figures and tables with captions and numbering per the template
- Keep the report consistent with the PDF style instructions

## Suggested Workflow
1. **Review the PDF template** and map each required page/section.
2. **Split content**: Teammate 1 owns backend sections, Teammate 2 owns frontend sections.
3. **Write draft text** in a shared document or markdown file.
4. **Collect visuals**: block diagrams, screenshots, sample API flows.
5. **Assemble final PDF** using Word/LaTeX/Google Docs matching the template.
6. **Cross-check** every chapter against the template headings.

## Report Delivery
The final report should be produced in the model and format of `TISD_Mini_Project_report_format.pdf` and should include:
- cover page details
- approval sheet with team members and supervisor
- declaration page
- abstract
- acknowledgments
- properly numbered chapters
- references and appendices

## Notes for Report Writers
- The backend team should provide exact feature descriptions for the report: current system supports one compost machine, one microcontroller, MQTT ingestion, batch progress logic, alerts, and farmer request module.
- The frontend team should provide UI/UX descriptions, mobile readiness, notification handling, and disabled auth implementation.
- Use the integration plan and API contract from `FRONTEND_AI_TASKS.md` as the technical reference.

---

This instruction file is your complete guide to writing the report across the two teammates using the root PDF format.