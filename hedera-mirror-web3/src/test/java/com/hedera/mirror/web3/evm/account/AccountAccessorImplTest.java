package com.hedera.mirror.web3.evm.account;

/*-
 * ‌
 * Hedera Mirror Node
 * ​
 * Copyright (C) 2019 - 2023 Hedera Hashgraph, LLC
 * ​
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ‍
 */

import static com.google.protobuf.ByteString.EMPTY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.google.protobuf.ByteString;

import com.hedera.mirror.web3.evm.store.contract.MirrorEntityAccess;

import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountAccessorImplTest {

    private static final String HEX = "0x00000000000000000000000000000000000004e4";
    private static final Address ADDRESS = Address.fromHexString(HEX);
    private static final Bytes BYTES = Bytes.fromHexString(HEX);
    private static final byte[] DATA = BYTES.toArrayUnsafe();

    @Mock
    private MirrorEntityAccess mirrorEntityAccess;
    @InjectMocks
    public AccountAccessorImpl accountAccessor;

    @Test
    void isTokenAddressTrue() {
        when(mirrorEntityAccess.isTokenAccount(ADDRESS)).thenReturn(true);
        final var result = accountAccessor.isTokenAddress(ADDRESS);
        assertThat(result).isTrue();
    }

    @Test
    void isTokenAddressFalse() {
        when(mirrorEntityAccess.isTokenAccount(ADDRESS)).thenReturn(false);
        final var result = accountAccessor.isTokenAddress(ADDRESS);
        assertThat(result).isFalse();
    }

    @Test
    void canonicalAddressIsUsableTrue() {
        when(mirrorEntityAccess.isUsable(ADDRESS)).thenReturn(true);
        final var result = accountAccessor.canonicalAddress(ADDRESS);
        assertThat(result).isEqualTo(ADDRESS);
    }

    @Test
    void canonicalAddressIsUsableFalse() {
        when(mirrorEntityAccess.isUsable(ADDRESS)).thenReturn(false);
        final var result = accountAccessor.canonicalAddress(ADDRESS);
        assertThat(result).isEqualTo(ADDRESS);
    }

    @Test
    void isExtantTrue() {
        when(mirrorEntityAccess.isExtant(ADDRESS)).thenReturn(true);
        when(mirrorEntityAccess.alias(ADDRESS)).thenReturn(EMPTY);
        final var result = accountAccessor.canonicalAddress(ADDRESS);
        assertThat(result).isEqualTo(ADDRESS);
    }

    @Test
    void alias() {
        when(mirrorEntityAccess.isExtant(ADDRESS)).thenReturn(true);
        when(mirrorEntityAccess.alias(ADDRESS)).thenReturn(ByteString.copyFrom(DATA));
        final var result = accountAccessor.canonicalAddress(ADDRESS);
        assertThat(result).isEqualTo(ADDRESS);
    }

    @Test
    void aliasDifferentFromEvmAddressSize() {
        when(mirrorEntityAccess.isExtant(ADDRESS)).thenReturn(true);
        when(mirrorEntityAccess.alias(ADDRESS)).thenReturn(ByteString.copyFrom(new byte[32]));
        final var result = accountAccessor.canonicalAddress(ADDRESS);
        assertThat(result).isEqualTo(ADDRESS);
    }
}
