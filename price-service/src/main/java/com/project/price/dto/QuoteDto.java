package com.project.price.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuoteDto {

  private double c;   // current price
  private double d;   // change
  private double dp;  // percent change
  private double h;   // high of day
  private double l;   // low of day
  private double o;   // open of day
  private double pc;  // previous close
}
