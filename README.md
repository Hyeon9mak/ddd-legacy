# 키친포스

## 퀵 스타트

```sh
cd docker
docker compose -p kitchenpos up -d
```

## 요구 사항

### 메뉴그룹(MenuGroup)
- [x] 새로운 메뉴그룹을 생성할 수 있다.
  - [x] 메뉴그룹 이름은 공백일 수 없다.
- [x] 전체 메뉴그룹을 조회할 수 있다.

### 메뉴(Menu)
- [x] 새로운 메뉴를 생성할 수 있다.
  - [x] 메뉴 가격은 공백 또는 음수일 수 없다.
  - [x] 메뉴 가격은 (상품의 금액 * 수량)의 총합보다 작거나 같아야한다.
  - [x] 메뉴가 속할 메뉴그룹이 우선 존재해야 한다.
  - [x] 메뉴에는 1개 이상의 상품이 포함되어야 한다.
  - [x] 메뉴 생성요청에 주어진 상품이 우선 존재해야 한다.
  - [x] 메뉴 생성요청에 주어진 상품당 수량은 음수일 수 없다.
  - [x] 메뉴 이름은 공백 또는 욕설을 포함할 수 없다.
- [x] 메뉴 가격을 변경할 수 있다.
  - [x] 변경할 메뉴가 우선 존재해야 한다.
  - [x] 새로운 메뉴 가격은 공백 또는 음수일 수 없다.
  - [x] 새로운 메뉴 가격은 (상품의 금액 * 수량)의 총합보다 작거나 같아야한다.
- [x] 메뉴를 사용자에게 노출되도록 변경할 수 있다.
  - [x] 노출할 메뉴가 우선 존재해야 한다.
  - [x] 메뉴 가격은 (상품의 금액 * 수량)의 총합보다 작거나 같아야한다.
- [ ] 메뉴를 사용자에게 노출되지 않도록 감출 수 있다.
  - [ ] 감출 메뉴가 우선 존재해야 한다.
- [ ] 전체 메뉴를 조회할 수 있다.

### 주문(Order)
- [ ] 새로운 주문을 생성할 수 있다.
  - [ ] 주문 생성요청 속 메뉴가 우선 존재해야한다.
  - [ ] 메뉴가 전시(노출) 중이어야 한다.
  - [ ] 주문에는 1개 이상의 메뉴가 포함되어야 한다.
  - [ ] 주문 생성요청의 메뉴 종류와 실제 메뉴 종류가 일치해야한다.
  - [ ] 주문 생성요청의 메뉴 수량은 음수일 수 없다.
  - [ ] 주문 생성요청의 메뉴 금액과 실제 메뉴 금액이 일치해야한다.
  - [ ] 주문 생성요청의 주문형태가 올바르지 않으면 생성할 수 없다.
  - [ ] 주문형태가 배달일 경우, 주소가 공백이거나 공백일 수 없다.
  - [ ] 주문형태가 매장식사일 경우, 주문테이블이 우선 존재해야 한다.
  - [ ] 주문형태가 매장식사일 경우, 주문테이블이 비어있어야 한다.
- [ ] 주문상태를 접수완료로 변경할 수 있다.
  - [ ] 주문이 우선 존재해야 한다.
  - [ ] 기존 주문상태가 대기중이 아닌 다른 상태면 접수완료로 변경할 수 없다.
  - [ ] 기존 주문형태가 배달일 경우 라이더에게 배달요청을 보낸다.
- [ ] 주문상태를 준비완료 변경할 수 있다.
  - [ ] 주문이 우선 존재해야 한다.
  - [ ] 기존 주문상태가 접수완료가 아닐 경우 준비완료로 변경할 수 없다.
- [ ] 주문상태를 배달중으로 변경할 수 있다.
  - [ ] 주문이 우선 존재해야 한다.
  - [ ] 기존 주문형태가 배달이어야 한다.
  - [ ] 기존 주문상태가 준비완료가 아닐일 경우 배달중으로 변경할 수 없다.
- [ ] 주문상태를 배달완료로 변경할 수 있다.
  - [ ] 주문이 우선 존재해야 한다.
  - [ ] 기존 주문상태가 배달중이 아닐 경우 배달완료로 변경할 수 없다.
- [ ] 주문상태를 처리완료로 변경할 수 있다.
  - [ ] 주문이 우선 존재해야 한다.
  - [ ] 주문형태가 배달일 때, 주문상태가 배달완료가 아니라면 처리완료로 변경할 수 없다.
  - [ ] 주문형태가 테이크아웃 또는 매장식사일 때, 준비완료가 아니라면 처리완료로 변경할 수 없다.
  - [ ] 주문형태가 매장식사일 때, 주문테이블의 상태를 초기화한다.
- [ ] 전체 주문을 조회할 수 있다.

### 주문테이블(OrderTable)
- [ ] 새로운 주문테이블을 생성할 수 있다.
  - [ ] 주문테이블의 이름은 공백이거나 공백일 수 없다.
  - [ ] 주문테이블의 초기 손님 수는 0명, 자리상태는 비어있음이다.
- [ ] 주문테이블의 자리상태를 채워짐으로 바꿀 수 있다.
  - [ ] 주문테이블이 우선 존재해야 한다.
- [ ] 주문테이블의 자리상태를 비어있음으로 바꿀 수 있다.
  - [ ] 주문테이블이 우선 존재해야 한다.
  - [ ] 비어있음으로 바뀔 경우 손님 수는 0명이 된다.
  - [ ] 주문테이블의 주문이 처리완료 상태가 아닐 경우 자리상태를 비어있음으로 바꿀 수 없다.
- [ ] 주문테이블의 손님 수를 변경할 수 있다.
  - [ ] 주문테이블이 우선 존재해야한다.
  - [ ] 변경하려는 손님 수는 음수일 수 없다.
  - [ ] 주문테이블의 상태가 우선 채워짐이어야 한다.
- [ ] 전체 주문테이블을 조회할 수 있다.

### 상품(Product)
- [x] 새로운 상품을 생성할 수 있다.
  - [x] 상품 금액은 공백이거나 음수일 수 없다.
  - [x] 상품 이름은 공백이거나 욕설을 포함할 수 없다.
- [x] 상품의 금액을 변경할 수 있다.
  - [x] 상품이 우선 존재해야 한다.
  - [x] 변경하려는 금액은 공백이거나 음수일 수 없다.
  - [ ] 상품의 금액이 변한 후, 상품이 속한 메뉴의 금액이 모든 상품의 금액보다 높을 경우 메뉴 전시(노출)을 중단한다.
- [x] 전체 상품을 조회할 수 있다.

## 용어 사전

| 한글명   | 영문명           | 설명                                                                                                       |
|-------|---------------|----------------------------------------------------------------------------------------------------------|
| 메뉴    | Menu          | 판매를 위해 전시(노출)되는 상품의 정보                                                                                   |
| 상품    | Product       | 메뉴에 대한 이름, 가격 메타정보                                                                                       |
| 메뉴_상품 | MenuProduct   | 메뉴와 상품간 연관관계 표현, 메뉴에 포함된 상품의 수량 정보                                                                       |
| 메뉴그룹  | MenuGroup     | 메뉴를 정렬하기 위한 그룹                                                                                           |
| 주문테이블 | OrderTable    | 가게 테이블에 대한 테이블명, 인원수, 손님유무 여부 메타정보                                                                       |
| 주문    | Order         | 주문에 주문방식, 상태, 주문시각, 배송지, 테이블에 대한 메타정보                                                                    |
| 주문형태  | OrderType     | 배달(DELIVERY), 테이크아웃(TAKEOUT), 매장식사(EAT_IN)로 이루어진 주문형태                                                    |
| 주문상태  | OrderStatus   | 대기중(WAITING), 접수완료(ACCEPTED), 준비완료(SERVED), 배달중(DELIVERING), 배달완료(DELIVERED), 처리완료(COMPLETED)로 이루어진 주문상태 |
| 주문목록  | Orders        | 주문 목록                                                                                                    |
| 주문아이템 | OrderLineItem | 주문에 포함된 메뉴와 메뉴의 수량, 금액 정보                                                                                |


## 모델링


<br>

# 문자열 계산기
## 기능 요구 사항
- [x] 쉼표(,) 또는 콜론(:)을 구분자로 가지는 문자열을 전달하는 경우 구분자를 기준으로 분리한 각 숫자의 합을 반환 (예: “” => 0, "1,2" => 3, "1,2,3" => 6, “1,2:3” => 6)
- [x] 앞의 기본 구분자(쉼표, 콜론) 외에 커스텀 구분자를 지정할 수 있다.
- [x] 커스텀 구분자는 문자열 앞부분의 “//”와 “\n” 사이에 위치하는 문자를 커스텀 구분자로 사용한다.
- [x] 예를 들어 “//;\n1;2;3”과 같이 값을 입력할 경우 커스텀 구분자는 세미콜론(;)이며, 결과 값은 6이 반환되어야 한다.
- [x] 문자열 계산기에 숫자 이외의 값 또는 음수를 전달하는 경우 RuntimeException 예외를 throw 한다.
- [x] 빈 문자열 또는 null 값을 입력할 경우 0을 반환해야 한다.
