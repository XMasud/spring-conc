package com.example.spring_conc.entity.enums;

import java.util.Collections;
import java.util.Set;

public enum OrderStatus {
    PENDING {
        @Override
        public Set<OrderStatus> getValidTransitions() {
            return Set.of(PROCESSING, CANCELED);
        }
    },
    PROCESSING {
        @Override
        public Set<OrderStatus> getValidTransitions() {
            return Set.of(SHIPPED, CANCELED);
        }
    },
    SHIPPED {
        @Override
        public Set<OrderStatus> getValidTransitions() {
            return Set.of(DELIVERED, CANCELED);
        }
    },
    DELIVERED {
        @Override
        public Set<OrderStatus> getValidTransitions() {
            return Collections.emptySet();
        }
    },
    CANCELED {
        @Override
        public Set<OrderStatus> getValidTransitions() {
            return Collections.emptySet();
        }
    };

    public abstract Set<OrderStatus> getValidTransitions();

    public boolean isValidStatus(OrderStatus currentStatus) {
        return this.getValidTransitions().contains(currentStatus);
    }
}
