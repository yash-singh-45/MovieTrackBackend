# 🎬 CineTrack — Backend

The backend API powering [CineTrack](https://movie-track-frontend.vercel.app/), a full-stack movie & TV show discovery platform. Built with Spring Boot, it handles authentication, movie/show data aggregation, and the review & rating system.

**🔗 Live Demo:** [movie-track-frontend.vercel.app](https://movie-track-frontend.vercel.app/)
**🎨 Frontend Repo:** [MovieTrackFrontend](https://github.com/yash-singh-45/MovieTrackFrontend)

---

## ✨ What it does

- Handles user authentication via **JWT**, including secure password.
- Manages user-submitted reviews and ratings with persistence in a relational database

## 🛠️ Tech Stack

| Layer | Tech |
|---|---|
| Framework | Spring Boot 4 (Java 21) |
| Auth | Spring Security + JWT (jjwt) |
| Data | Spring Data JPA, MySQL / PostgreSQL |
| Validation | Spring Validation |
| Mapping | ModelMapper |
| Email | Spring Boot Starter Mail |

## 📋 API Overview

> _Fill this in with your actual endpoints, e.g.:_

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Authenticate and receive a JWT |
| GET | `/api/movies/trending` | Get trending titles |
| GET | `/api/movies/{id}` | Get full details for a title |
| POST | `/api/movies/{id}/reviews` | Submit a review/rating |

## 🔗 Related

This is the backend half of CineTrack. The frontend (React, Vite, Tailwind) lives at [MovieTrackFrontend](https://github.com/yash-singh-45/MovieTrackFrontend).
