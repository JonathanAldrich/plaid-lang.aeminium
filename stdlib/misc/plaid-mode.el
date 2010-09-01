;; Plaid Mode
;;
;; 1. copy plaid-mode.el into a place emacs can find it
;; 2. add (require 'plaid-mode) to .emacs file
;; 


(provide 'plaid-mode)


(defvar plaid-mode-hook nil)
(defvar plaid-mode-map
  (let ((map (make-keymap)))
    (define-key map "\C-j" 'newline-and-indent)
    map)
  "Keymap for Plaid major mode")


;; keywords 
(defvar plaid-keywords  '("type" "method" "var" "val" "package" "import" "state" "match" "default" "override" "case"  "with" "of" "new" "fn"))
(defvar plaid-vars      '("this"))
(defvar plaid-types     '("unique" "immutable" "shared" "unit" "Int" "String"))
(defvar plaid-constants '("true" "false")) 
(defvar plaid-keywords-regexp  (regexp-opt plaid-keywords  'words))
(defvar plaid-vars-regexp      (regexp-opt plaid-vars      'words))
(defvar plaid-types-regexp     (regexp-opt plaid-types     'words))
(defvar plaid-constants-regexp (regexp-opt plaid-constants 'words))
(defconst plaid-font-lock-keywords
  `(
    (,plaid-vars-regexp      . font-lock-variable-name-face)
    (,plaid-constants-regexp . font-lock-constant-face)
    (,plaid-keywords-regexp  . font-lock-keyword-face)
    (,plaid-types-regexp     . font-lock-type-face)
    )
  "Additional Keywords to highlight in Plaid mode")


(defvar plaid-mode-syntax-table
  (let ((st (make-syntax-table)))
    ;; allow underscore in words 
    (modify-syntax-entry ?_ "w" st)
    ;; comments
    (modify-syntax-entry ?/ ". 124b" st)
    (modify-syntax-entry ?* ". 23" st)
    (modify-syntax-entry ?\n "> b" st)
    st)
  "Syntax table for plaid-mode")


(defun plaid-mode ()
  "Major mode for editing Plaid Programming Language files"
  (interactive)
  (kill-all-local-variables)
  (set-syntax-table plaid-mode-syntax-table)
  (use-local-map plaid-mode-map)
  (set (make-local-variable 'font-lock-defaults) '(plaid-font-lock-keywords))
  (setq major-mode 'plaid-mode)
  (setq mode-name "PLAID")
  (run-hooks 'plaid-mode-hook))

;; always activate this mode with Plaid files 
(add-to-list 'auto-mode-alist '("\\.plaid\\'" . plaid-mode))
