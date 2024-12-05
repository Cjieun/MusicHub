package com.example.musicHub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
    private long id;        // Like 고유 ID
    private long userId;    // 좋아요를 누른 사용자 ID
    private long musicId;   // 좋아요를 받은 음악 ID
}
