package com.sn.snuser.dto;

import java.util.List;

public record ContactsDto(List<String> mobiles, List<String> emails, List<String> sites) {
}
