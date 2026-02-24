package com.omnisupport.tickets.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataTicketRepository extends JpaRepository<TicketEntity, Long> {
}
