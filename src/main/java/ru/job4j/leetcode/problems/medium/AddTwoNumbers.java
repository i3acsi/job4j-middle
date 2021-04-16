package ru.job4j.leetcode.problems.medium;

/**
 * You are given two non-empty linked lists representing two non-negative integers.
 * The digits are stored in reverse order, and each of their nodes contains a single digit.
 * Add the two numbers and return the sum as a linked list.
 * <p>
 * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 * Example 1:
 * 2->4->3
 * 5->6->4
 * -------
 * 7->0->8
 * Input: l1 = [2,4,3], l2 = [5,6,4]
 * Output: [7,0,8]
 * Explanation: 342 + 465 = 807.
 * <p>
 * Example 2:
 * Input: l1 = [0], l2 = [0]
 * Output: [0]
 * <p>
 * Example 3:
 * Input: l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
 * Output: [8,9,9,9,0,0,0,1]
 * <p>
 * Constraints:
 * - The number of nodes in each linked list is in the range [1, 100].
 * - 0 <= Node.val <= 9
 * - It is guaranteed that the list represents a number that does not have leading zeros.
 * <p>
 * <p>
 * Definition for singly-linked list.
 * struct ListNode {
 * int val;
 * ListNode *next;
 * ListNode() : val(0), next(nullptr) {}
 * ListNode(int x) : val(x), next(nullptr) {}
 * ListNode(int x, ListNode *next) : val(x), next(next) {}
 * };
 **/
public class AddTwoNumbers {
    ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(0);
        ListNode p = l1, q = l2, curr = dummyHead;
        int carry = 0;
        while (p != null || q != null) {
            int x = (p != null) ? p.val : 0;
            int y = (q != null) ? q.val : 0;
            int sum = carry + x + y;
            carry = sum / 10;
            curr.next = new ListNode(sum % 10);
            curr = curr.next;
            if (p != null) p = p.next;
            if (q != null) q = q.next;
        }
        if (carry > 0) {
            curr.next = new ListNode(carry);
        }
        return dummyHead.next;
    }

    public static void main(String[] args) {
        AddTwoNumbers addTwoNumbers = new AddTwoNumbers();
        ListNode input1 = new ListNode(2, new ListNode(4, new ListNode(3)));
        ListNode input2 = new ListNode(5, new ListNode(6, new ListNode(4)));

        ListNode result = addTwoNumbers.addTwoNumbers(input1, input2);
        System.out.println("expected: 708");
        System.out.print("  result: ");
        while (result != null) {
            System.out.print(result.val);
            result = result.next;
        }

        System.out.println();
        input1 = new ListNode();
        input2 = new ListNode();
        result = addTwoNumbers.addTwoNumbers(input1, input2);
        System.out.println("expected: 0");
        System.out.print("  result: ");
        while (result != null) {
            System.out.print(result.val);
            result = result.next;
        }

        System.out.println();
        input1 = new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9)))))));
        input2 = new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9))));
        result = addTwoNumbers.addTwoNumbers(input1, input2);
        System.out.println("expected: 89990001");
        System.out.print("  result: ");
        while (result != null) {
            System.out.print(result.val);
            result = result.next;
        }
    }
}

class ListNode {
    int val;
    ListNode next;

    public ListNode() {
        this.val = 0;
        this.next = null;
    }

    public ListNode(int x) {
        this.val = x;
        this.next = null;
    }

    public ListNode(int x, ListNode next) {
        this.val = x;
        this.next = next;
    }
}

