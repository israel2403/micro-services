package com.huerta.accounts.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Account extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", columnDefinition = "UUID")
  private UUID accountId;

  @Column(name = "account_number", nullable = false, unique = true)
  private int accountNumber;

  @Column(name = "account_type", nullable = false, length = 100)
  private String accountType;

  @Column(name = "branch_address", nullable = false, length = 200)
  private String branchAddress;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "customer_id", referencedColumnName = "id")
  private Customer customer;

  public Account(final Customer customer) {
    this.customer = customer;
  }
}
